# ---- Estágio 1: Build ----
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Copia primeiro os arquivos de configuração do Maven Wrapper
# (aproveita cache do Docker se as dependências não mudarem)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Baixa as dependências (cacheável)
RUN ./mvnw dependency:go-offline -B

# Copia o restante do código-fonte
COPY src src

# Compila o projeto, gerando o .jar (pula os testes para acelerar o build de deploy)
RUN ./mvnw clean package -DskipTests

# ---- Estágio 2: Runtime ----
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copia apenas o .jar final do estágio de build (imagem final fica bem menor)
COPY --from=build /app/target/*.jar app.jar

# Porta padrão do Spring Boot (ajuste se seu application.yml usar outra)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
