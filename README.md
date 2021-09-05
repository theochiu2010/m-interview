# m-interview

## Tech stack
* Java
* Spring boot
* Hibernate
* H2

## How to run
If you have maven configured, you can run the application using
mvn spring-boot:run

If you are using an IDE, you can run the application directly by running 
AccountManagerApplication

## Available API
### Get Account Balance request example
### http://localhost:8080/accounts/12345678/balance

### response example:
{
"acctNumber": "12345678",
"balance": 995000.0
}

### Create Account Transfer request example
### http://localhost:8080/accounts/transfer
{
    "fromAcctNumber": "12345678",
    "toAcctNumber": "88888888",
    "amount": 5000.0
}

### response example:
{
"message": "The transfer was successful"
}

