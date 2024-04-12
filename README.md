#  LangChain4j in Jakarta EE and MicroProfile


This example demonstrates LangChain4J in a Jakarta EE / MicroProfile application on [Helidon](https://helidon.io/docs/v4/about/doc_overview). The application is a chatbot built with LangChain4J and uses Jakarta CDI, Jakarta RESTful Web Services, Jakarta WebSocket, MicroProfile Config, MicroProfile Metrics, MicroProfile Health and MicroProfile OpenAPI features.

This is an adaptation of the [OpenLiberty example](https://github.com/langchain4j/langchain4j-examples/tree/main/jakartaee-microprofile-example). Thanks to the portability, the main code has been kept "as is", only the test code has been slightly adapted (and of cousre microprofile-config.properties and pom.xml to fit with Helidon).

## Prerequisites:

- Java 21
- Hugging Face API Key
  - Sign up and log in to https://huggingface.co.
  - Go to [Access Tokens](https://huggingface.co/settings/tokens). 
  - Create a new access token with `read` role.
- Optionnally [Helidon CLI](https://helidon.io/docs/v4/about/cli). An alternative is to install it with [sdkman](https://sdkman.io/sdks)

## Environment Set Up

To run this example application, navigate  to the jakartaee-microprofile-example-helidon directory:

```bash
cd jakartaee-microprofile-example-helidon
```

Set the following environment variables:

```bash
export JAVA_HOME=<your Java home path>
export HUGGING_FACE_API_KEY=<your Hugging Face read token>
```

## Start the application

If Helidon CLI is installed, you can run the application in dev mode:

```bash
helidon dev
```

Altenatively, you can package and run it:
```bash
# Do it once
mvn package
# Run as many times as you want
java -jar target/jakartaee-microprofile-example-helidon.jar
```

## Try out the application

To chat using WebSocket:

- Navigate to http://localhost:8080
- At the prompt, try the following message examples:
  - What are large language models?
    
  - Which are the most used models?
    
    
## Try out other models

To interract with other models, using REST, navigate to the the [OpenAPI UI](http://localhost:8080/openapi/ui/index.html) URL for the following 3 REST APIs:

- [HuggingFaceLanguageModel](https://github.com/langchain4j/langchain4j/blob/main/langchain4j-hugging-face/src/main/java/dev/langchain4j/model/huggingface/HuggingFaceLanguageModel.java)
  - Expand the GET `/api/chat/language` API.
    1. Click the **Try it out** button.
    2. Type `When was langchain4j launched?`, or any question, in the question field.
    3. Click the **Execute** button.
  - Alternatively, run the following `curl` command from a command-line session:
    - ```
      curl 'http://localhost:8080/api/model/language?question=When%20was%20langchain4j%20launched%3F'```
- [HuggingFaceChatModel](https://github.com/langchain4j/langchain4j/blob/main/langchain4j-hugging-face/src/main/java/dev/langchain4j/model/huggingface/HuggingFaceChatModel.java)
  - expand the GET `/api/model/language` API
    1. Click the **Try it out** button.
    2. Type `Which are the most used Large Language Models?`, or any question, in the question field.
    3. Click the **Execute** button.
  - Alternatively, run the following `curl` command from a command-line session:
    - ```
      curl 'http://localhost:8080/api/model/chat?userMessage=Which%20are%20the%20most%20used%20Large%20Language%20models%3F' | jq```
- [InProcessEmbeddingModel](https://github.com/langchain4j/langchain4j-embeddings)
  - expand the GET `/api/model/similarity` API
    1. Click the **Try it out** button.
    2. Type `I like Jakarta EE and MicroProfile.`, or any text, in the the **text1** field.
    3. Type `I like Python language.`, or any text, in the the **text2** field. 
    3. Click the **Execute** button.
  - Alternatively, run the following `curl` command from a command-line session:
    - ```
      curl 'http://localhost:8080/api/model/similarity?text1=I%20like%20Jakarta%20EE%20and%20MicroProfile.&text2=I%20like%20Python%20language.' | jq```

## Running the tests
```bash
mvn test
```



## Try metrics

Prometheus Format:
```
curl -s -X GET http://localhost:8080/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .
```
JSON Format:
```
curl -H 'Accept: application/json' -X GET http://localhost:8080/metrics
{"base":...
. . .
```


## Try health

```
curl -s -X GET http://localhost:8080/health
{"outcome":"UP",...
```
