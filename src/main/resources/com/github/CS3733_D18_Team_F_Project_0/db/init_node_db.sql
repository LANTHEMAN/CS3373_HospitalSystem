
Create Table Node (
id varchar(15) Primary Key,
x_coord REAL not NULL,
y_coord REAL not NULL,
floor char(2),
building varchar(10),
nodeType char(4),
longName varchar(50),
shortName varchar(30),
xcoord3d REAL not NULL,
ycoord3d REAL not NULL
)