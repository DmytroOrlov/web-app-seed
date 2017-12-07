Build application Docker image (Scala script requires Ammonite):
```sh
./Build.sc
```

Install Ammonite (needed to run scripts):
```sh
brew install ammonite-repl
```

Run application in Docker:
```sh
docker run --rm -p 9000:9000 --env "SECRET_KEY=$(sbt -Dsbt.log.noformat=true 'set showSuccess:=false' playGenerateSecret | tail -n 2 | head -n 1 | cut -d ' ' -f 5-)" web-app
```

Verify Kubernetes service recovery in integration test (require minikube, helm):
```sh
./ItTest.sc
```
