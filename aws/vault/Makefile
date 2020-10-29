
validate:
	echo "AWS_ACCESS_KEY_ID ${AWS_ACCESS_KEY_ID}"
	echo "AWS_SECRET_ACCESS_KEY ${AWS_SECRET_ACCESS_KEY}"
	packer validate vault.json

build: validate
	packer build vault.json
