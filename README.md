# Functionality:

- ### you have CRUD operations for Discount cards and Products
- ### you can generate cheque using the following URL:
> http://localhost:8080/cheque?[arguments]
- ### you can get cheque in PDF format using the following URL:
> http://localhost:8080/pdf/cheque?[arguments]

## Arguments template:
> productId=quantity&productId=quantity...&card=17

# Before running the application, you should know:
- ### there are 100 discount cards, their IDs are from 1 to 100
- ### there are 10 items, their IDs are from 1 to 10
- ### products are validated by barcode field (example: 111AAA11AA)
- ### database initialization depends on boolean *database.initialize* property 

## In order to run database you should run the following command:
> docker-compose up