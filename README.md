
Бекэнд, созданный на стеке: Java 1.8 + Spring Boot + Maven + MySQl80. Для запуска данного кода, нужно:

1) Установить программу Intelij Idea Ultimate от компании JetBrains(чтобы скачать эту программу перейдите по следующей ссылке: https://www.jetbrains.com/idea) (я создавал этот код в Intelij Idea Ultimate 2021.3.1).
2) После установки Intelij Idea Ultimate создайте свой аккаунт на Jetbrains: https://account.jetbrains.com.
3) Установить весь пакет MySQl Community. Ссылка на установщик: https://drive.google.com/file/d/16H1Rk5T_LNfGPudQ9fSEIA80GdUkWLi2/view?usp=sharing
4) Переменные окружения для связи Backend с Бд MySQl находятся в файле application.properties: 
spring.jpa.hibernate.ddl-auto=update 
spring.profiles.active=@spring.profiles.active@ 
spring.jpa.open-in-view=false 
spring.servlet.multipart.max-file-size=300MB 
spring.servlet.multipart.max-request-size=300MB 
spring.servlet.multipart.enabled=true 
spring.datasource.url=jdbc:mysql://localhost:3306/socialmedia
spring.datasource.username=root spring.datasource.password=iamroot

5) Данные переменные окружения установлены мною по умолчанию, либо создайте свои путём создания новой схемы в MySQL Workbench c логином и паролем, полученными при установке MySQl Community.
6) Запустите програму в Intelij Idea Ultimate.
7) Документация Swagger появится на экране в браузере сразу после запуска программу по адресу: http://localhost:8080/swagger-ui.html

