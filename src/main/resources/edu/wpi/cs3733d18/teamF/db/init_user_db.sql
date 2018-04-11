Create Table HUser (
  userType varchar(20) not null,
  username varchar(20) Primary Key,
  password varchar(20),
  firstName VARCHAR(20),
  lastName VARCHAR(20),
  privilege varchar(20),
  occupation varchar(20)
);