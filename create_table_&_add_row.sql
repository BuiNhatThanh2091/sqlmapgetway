CREATE TABLE [dbo].[Users] (
    UserID     INT IDENTITY(1,1) PRIMARY KEY,   -- tự tăng từ 1
    Email      NVARCHAR(255) NOT NULL UNIQUE,   -- không trùng email
    FirstName  NVARCHAR(100) NOT NULL,
    LastName   NVARCHAR(100) NOT NULL
);

INSERT INTO [dbo].[Users] (Email, FirstName, LastName)
VALUES 
('nhatthanh@gmail.com', 'Nhat', 'Thanh'),
('hehe@murach.com', 'Hehe', 'Hihi'),
('haha@yahoo.com', 'Hahaha', 'Haha'),
('huhu@murach.com', 'Huhu', 'Hoho');
