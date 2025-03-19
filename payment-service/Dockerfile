# Sıfır bir linux makinemiz var, projeyi nasıl kurarız?

# Öncelikle docker hub'a gidilip neleri kurmamız gerekiyorsa onlar alınır.
# makineye maven kurar.
FROM maven:3.9.6-eclipse-temurin-21 AS build

#işlem yapılacak klasörü belirleriz
WORKDIR /app

# Daha sonra projemizi ilgili dosynın altına kopyalarız, ( . ) şuan bulunduğum noktadaki her şey anlamına gelir.
# (sağ taraf: source, sol taraf: destination)
COPY . /app/

# mvn clean: Projenin önceki derlemelerinden kalan target klasörünü temizler, eski build dosyalarını siler
# package: Projeyi derler, Jar/War dosyası üretir
# -DskipTests: Test adımlarını atlar
RUN mvn clean package -DskipTests

# Önceki kısım projenin derlenmesi içindi. Konteynr projenin çalışmasını da amaçlar, aşağıda yapılır.

FROM eclipse-temurin:21-jdk
COPY --from=build /app/target/order-service-0.0.1-SNAPSHOT.jar /app/order-service.jar

#RUN: Image inşa edilirken "hazırlık" yapar
#ENTRYPOINT: Konteyner başlatılınca "çalıştırma" yapar

ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]

# Docker build işlemini başlatmak için de jar dosyasının bulunduğu konuma gidilir ve:
#   docker build -t turkcell-order-service .
# -t : tag işlemi yapar
#( . ) : şu anda bulunduğum konum anlamına gelir, yerine koum da yazılabilir