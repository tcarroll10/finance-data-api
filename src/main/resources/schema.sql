create table if not exists Security (
  record_date DATE,
  security_type_desc varchar(50) not null,
  security_desc varchar(50) not null,
  avg_interest_rate_amt DECIMAL(5, 3)not null 

);

create table if not exists TestTable (
  record_date DATE,
  security_type_desc varchar(50) not null,
  security_desc varchar(50) not null,
  avg_interest_rate_amt DECIMAL(5, 3)not null 

);

create table if not exists UserTable (
  name varchar(50),
  id integer
  

);



