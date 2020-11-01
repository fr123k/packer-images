export VAGRANT_EXPERIMENTAL ?= cloud_init,disks
PACKER_BUILDER ?= amazon-ebs.vault
PACKER_FILE ?= vault.pkr.hcl
validate:
	packer validate -only=${PACKER_BUILDER} ${PACKER_FILE}

build: validate
	PACKER_LOG=1 packer build --debug -only=${PACKER_BUILDER} ${PACKER_FILE}
