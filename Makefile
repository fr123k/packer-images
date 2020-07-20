validate: ## Build the jenkins agents as docker images.
	$(MAKE) -C aws validate

build: validate ## Build the jenkins agents as docker images.
	$(MAKE) -C aws build
