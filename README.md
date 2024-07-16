# Url shortener

Encurtador de urls

Serviço que encurta URLs longas e torna elas compactas e fáceis de compartilhar.

# Tecnologias utilizadas

- Spring Boot
- Spring Data MongoDB
- Docker
- Swagger API
- Testes com JUnit 5

# Como executar

- Clonar repositório
- Criar docker container

# API Endpoints

Enviar um body com a URL que deseja encurtar

````java
POST http:localhost/8080/shorten-url


{
  "url": "https://www.youtube.com/"
}

```

O retorno será um JSON com a URL encurtada: 

```java

{
    "url": "http://localhost:8080/jgPbX"
}

```

# Considerações

- O encurtador é composto por um minimo de 05 e maximo de 10 caracteres.
