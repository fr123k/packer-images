export VAGRANT_EXPERIMENTAL ?= cloud_init,disks
PACKER_BUILDER ?= amazon-ebs.vault
PACKER_FILE ?= vault.pkr.hcl
validate:
	packer validate -only=${PACKER_BUILDER} ${PACKER_FILE}

build: validate
	packer build -only=${PACKER_BUILDER} ${PACKER_FILE}
