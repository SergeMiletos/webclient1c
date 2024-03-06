The main purpose of this project was to get a plain table from external database and transform it to the pivot table with columns whose composition depends on the data source. Since 1C:Enterprise subsystems (especially in older versions) have no (or over-complicated and slow) solutions, this one seems to be much faster in every way. Dynamic columns composition is implemented with JSONB type field.

But then the project grew a little bit. Now it is a Tapestry5/Hibernate web application, recieving data from 1C:Enterprise http services, syncing them with the internal PostgreSQL database. It implements the minimum functionality of the sales manager's workplace: viewing and filtering the stock of goods, adding invoice lines; selecting the company, partner and related contract from the corresponding lists; sending the data entered by the user to the server and receiving back a pdf-file as a printed form of the created invoice.


SQL folder - here are ddl's for PostgreSQL functions.
1C - 1C:Enterprise partial configuration with the implemented http-services modules 

TODO:
- Create mock data source: both http and database
- Optimize data syncronization size
- User authorisation implemented, but not used
- And other more than 200 items on the list

06.03.2024

