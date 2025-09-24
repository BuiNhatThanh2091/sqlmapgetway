-- (Nếu login CHƯA tồn tại ở server)
IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = 'test-web-sqlgateway')
    CREATE LOGIN [test-web-sqlgateway] WITH PASSWORD = '123456';
GO

-- Ánh xạ login vào DB SQLGateway → thành user
USE [SQLGateway];
GO
IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = 'test-web-sqlgateway')
    CREATE USER [test-web-sqlgateway] FOR LOGIN [test-web-sqlgateway];
GO

-- Cấp quyền (đọc/ghi cơ bản)
EXEC sp_addrolemember N'db_datareader', N'test-web-sqlgateway';
EXEC sp_addrolemember N'db_datawriter', N'test-web-sqlgateway';
GO
