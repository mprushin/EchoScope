# EchoScope

## Loader
Application loads articles from echo.msk.ru and stores them to Amazon S3 bucket.

## Analytics
Calculates count of each word with length greater than 3 symbols in all articles.

## Configuration
Set up environment variables:
```
AWS_ACCESS_KEY - amazon access key
AWS_SECRET_KEY - amazon secret key 
BUCKET_NAME - amazon S3 buckt name (should be created beforehand)
PROXY_HOST - proxy host if needed
PROXY_PORT - proxy port if needed
```

## Running
```
Loader: sbt runMain Loader
Analytics: sbt runMain Analytics
```
