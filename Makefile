APP_NAME=rulojuka/libridge-backend

all: integration_tests

clean:
	mvn clean
	rm -f ./libridge-server.jar
	docker rmi $(APP_NAME); true

package: kill_server mvn_package copy_jar

integration_tests: kill_server mvn_integration_tests copy_jar

kill_server:
	@./kill_server.sh

mvn_package:
	mvn clean package

mvn_integration_tests:
	mvn -Dspring-boot.run.profiles=development verify

run_development:
	java -Dspring.profiles.active=development -jar libridge-server.jar

copy_jar:
	cp target/libridgebackend-1.0.0-alpha.jar ./libridge-server.jar

build:
	docker build -t $(APP_NAME) .

deploy: build
	docker login
	docker push $(APP_NAME)

run:
	docker run -p 8080:8080 --rm $(APP_NAME)
