# This file was autogenerated by the BETA 'packer hcl2_upgrade' command. We
# recommend double checking that everything is correct before going forward. We
# also recommend treating this file as disposable. The HCL2 blocks in this
# file can be moved to other files. For example, the variable blocks could be
# moved to their own 'variables.pkr.hcl' file, etc. Those files need to be
# suffixed with '.pkr.hcl' to be visible to Packer. To use multiple files at
# once they also need to be in the same folder. 'packer inspect folder/'
# will describe to you what is in that folder.

# All generated input variables will be of 'string' type as this is how Packer JSON
# views them; you can change their type later on. Read the variables type
# constraints documentation
# https://www.packer.io/docs/from-1.5/variables#type-constraints for more info.
variable "aws_access_key" {
  type    = string
  # default = "${var.AWS_ACCESS_KEY_ID}"
}

variable "aws_secret_key" {
  type    = string
  # default = "${var.AWS_SECRET_ACCESS_KEY}"
}

# "timestamp" template function replacement
locals { timestamp = regex_replace(timestamp(), "[- TZ:]", "") }

source "amazon-ebs" "vault" {
  access_key        = var.aws_access_key
  secret_key        = var.aws_secret_key
  ami_name          = "vault-{{isotime \"02-Jan-06 03-04-05\"}}"
  instance_type     = "t2.medium"
  region            = "eu-west-1"
  security_group_id = "sg-344fe64c"
  source_ami_filter {
    filters = {
      name                = "ubuntu/images/hvm-ssd/ubuntu-xenial-16.04-amd64-server-20200129"
      root-device-type    = "ebs"
      virtualization-type = "hvm"
    }
    most_recent = true
    owners      = ["099720109477"]
  }
  ssh_username = "ubuntu"
  tags = {
    BUILD_TS_FORMATTED = "{{ isotime \"02-01-06_03-04-05\" }}"
    BUILD_TS_UNIX      = "{{ timestamp }}"
  }
  user_data_file = "cloud-init/cloud-init.yaml"
}

source "vagrant" "vault" {
  skip_add     = true
  add_force    = false
  communicator = "ssh"
  output_dir   = "vagrant/output"
  provider     = "virtualbox"
  source_path  = "ubuntu/bionic64"
  template     = "vagrant/vagrantfile.template"
}

# a build block invokes sources and runs provisioning steps on them. The
# documentation for build blocks can be found here:
# https://www.packer.io/docs/from-1.5/blocks/build
build {
  sources = ["source.amazon-ebs.vault", "source.vagrant.vault"]

  provisioner "shell" {
    inline = [
      "/usr/bin/cloud-init status --wait",
      "sudo systemctl stop cloud-init",
      "cat /var/log/cloud-init-output.log",
      "sudo ls -lha /var/lib/cloud/",
      "sudo rm -rfv /var/lib/cloud/"]
  }
  # provisioner "goss" {
  #   arch          = "amd64"
  #   download_path  = "/tmp/goss-VERSION-linux-ARCH"
  #   format        = ""
  #   goss_file     = ""
  #   password      = ""
  #   remote_folder = "/tmp"
  #   remote_path   = "/tmp/goss"
  #   retry_timeout = "0s"
  #   skip_install   = false
  #   skip_ssl      = false
  #   sleep         = "1s"
  #   tests         = ["goss/goss.yaml"]
  #   url           = "https://github.com/aelsabbahy/goss/releases/latest/download/goss-linux-amd64"
  #   use_sudo      = false
  #   username      = ""
  #   vars_env = {
  #     ARCH     = "amd64"
  #   }
  #   vars_file = ""
  #   vars_inline = {
  #     OS      = "centos"
  #   }
  #   version = "0.3.2"
  # }
}
