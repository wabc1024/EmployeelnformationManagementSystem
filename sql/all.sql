CREATE DATABASE emp_im_system CHARACTER SET utf8 COLLATE utf8_general_ci;

USE emp_im_system

#员工表
CREATE TABLE employees(
                          emp_id INT PRIMARY KEY AUTO_INCREMENT,
                          NAME VARCHAR(20) NOT NULL,
                          salary DOUBLE(8,2) NOT NULL,
                          department_id INT,
                          manager_id INT,
                          CONSTRAINT fk_emp_dept_id FOREIGN KEY(department_id) REFERENCES departments(department_id)
)

    INSERT INTO employees(`name`,salary,department_id,manager_id)
VALUES('张三',6000,2,4),
('小王',3000,1,4),
('小黄',5500,3,NULL),
('王五',7200,2,3),
('赵六',3300,1,4)

#部门表
CREATE TABLE departments(
                            department_id INT PRIMARY KEY AUTO_INCREMENT,
                            department_name VARCHAR(30) NOT NULL,
                            create_date DATE NOT NULL
)

    INSERT INTO departments(department_name,create_data)
VALUES('前端开发部',NOW()),
('后端开发部',NOW()),
('人事部',NOW())

#用户表
CREATE TABLE USER(
                     user_id INT PRIMARY KEY AUTO_INCREMENT,
                     emp_id INT,
                     user_name VARCHAR(20) UNIQUE NOT NULL,
                     user_pwd CHAR(32) NOT NULL,
                     create_date DATE NOT NULL,
                     user_type VARCHAR(20) NOT NULL,
                     is_deleted BOOL DEFAULT FALSE,
                     CONSTRAINT fk_user_emp_id FOREIGN KEY(emp_id) REFERENCES employees(emp_id)
)

    INSERT INTO USER(user_id,emp_id,user_name,user_pwd,create_date,user_type)
VALUES(1001,2,'admin',MD5(123456),NOW(),'admin')


#任务表
CREATE TABLE task(
                     id INT PRIMARY KEY AUTO_INCREMENT,
                     create_emp_id INT,
                     content VARCHAR(1000) NOT NULL,
                     create_date DATE NOT NULL,
                     CONSTRAINT fk_task_create_emp_id FOREIGN KEY(create_emp_id) REFERENCES employees(emp_id)
)

    INSERT INTO task(create_emp_id,content,create_date)
VALUES(5,'完成员工管理系统的mvc层开发',NOW())



SELECT e.emp_id AS empId,e.name,e.salary,d.department_name AS departmentName,mgr.name AS 'managerName'
FROM employees e LEFT JOIN departments d
                           ON e.department_id = d.department_id
                 LEFT JOIN employees mgr
                           ON e.manager_id = mgr.emp_id
WHERE e.emp_id = 2


UPDATE employees
SET NAME='赵六',salary=10000,department_id = (SELECT department_id FROM departments WHERE department_name = '后端开发部'),
    manager_id = (SELECT emp_id FROM employees WHERE NAME = '小黄')
WHERE emp_id = 5