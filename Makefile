APP_NAME=identity
IMAGE_NAME=identity-app
PORT=8081

build:
	docker build -t $(IMAGE_NAME) .

run:
	docker run -d \
		--name $(APP_NAME) \
		-p $(PORT):$(PORT) \
		--restart unless-stopped \
		$(IMAGE_NAME)

stop:
	docker stop $(APP_NAME) || true
	docker rm $(APP_NAME) || true

restart: stop build run

logs:
	docker logs -f $(APP_NAME)

status:
	docker ps | grep $(APP_NAME)
