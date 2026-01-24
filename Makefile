APP_NAME=identity
IMAGE_NAME=identity-app
PORT=8081
DOCKER=sudo docker

build:
	$(DOCKER) build -t $(IMAGE_NAME) .

run:
	$(DOCKER) run -d \
		--name $(APP_NAME) \
		-p $(PORT):$(PORT) \
		--restart unless-stopped \
		$(IMAGE_NAME)

stop:
	-$(DOCKER) stop $(APP_NAME)
	-$(DOCKER) rm $(APP_NAME)

restart: stop build run

logs:
	$(DOCKER) logs -f $(APP_NAME)

status:
	$(DOCKER) ps | grep $(APP_NAME)
