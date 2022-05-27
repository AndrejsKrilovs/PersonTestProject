create table person (
  id varchar(20),
  date_of_birth date not null,
  first_name varchar(30) not null,
  gender integer not null,
  last_name varchar(50) not null,
  primary key (id)
);