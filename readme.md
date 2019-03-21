сборка: mvn clean package

запуск:
 
    cd ./target
    java -jar user.service-0.1.jar
    
примеры запросов:

    добавить пользователя: 
        curl -X POST http://localhost:8080/user/save -d '{"firstName":"Kolya", "lastName":"Petrov","birthday":"2017-12-12",
    "email":"1@mail.ru", "password": "123"}' -H 'Content-Type: application/json'
    
    удалить пользователя:
        curl -X DELETE http://localhost:8080/user/1/delete
    
    найти по eamil:
        curl -X GET http://localhost:8080/user/find?email="1@mail.ru"
    