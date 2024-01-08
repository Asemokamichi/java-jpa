-- Схема для хранения товаров и характеристик товаров специфических для каждой
-- отдельно взятой категории.
--
-- В схеме должна быть возможность хранения категорий у каждой из которых может
-- быть свой перечень характеристик, например категория `Процессоры` с
-- характеристиками `Производитель`, `Количество ядер`, `Сокет` или категория
-- `Мониторы` с характеристиками `Производитель`, `Диагональ`, `Матрица`,
-- `Разрешение`.

-- Процессоры      -> Intel Core I9 9900 -> AMD Ryzen R7 7700
-- Производитель   -> Intel              -> AMD
-- Количество ядер -> 8                  -> 12
-- Сокет           -> 1250               -> AM4

-- Мониторы      -> Samsung SU556270 -> AOC Z215S659
-- Производитель -> Samsung          -> AOC
-- Диагональ     -> 27               -> 21.5
-- Матрица       -> TN               -> AH-IPS
-- Разрешение    -> 2560*1440        -> 1920*1080

drop table if exists product_characteristic;

drop table if exists products;

drop table if exists characteristics;

drop table if exists categories;

create table categories
(
    category_id   serial8 primary key,
    category_name varchar(50) not null
);


create table products
(
    product_id   serial8 primary key,
    category_id  int8 references categories (category_id),
    product_name varchar(20) not null,
    cost         int8        not null
);

create table characteristics
(
    characteristic_id    serial8 primary key,
    category_id          int8 references categories (category_id),
    characteristics_name varchar(50) not null
);

create table product_characteristic
(
    product_characteristic_id serial primary key,
    product_id                int8 references products (product_id),
    characteristic_id         int8 references characteristics (characteristic_id),
    characteristic            varchar(120) not null,
    unique (product_id, characteristic_id)
);

insert into categories (category_name)
values ('Процессоры'),
       ('Мониторы');

-- Процессоры characteristics
insert into characteristics (category_id, characteristics_name)
values (1, 'Производитель'),
       (1, 'Количество ядер'),
       (1, 'Сокет');

-- Мониторы characteristics
insert into characteristics (category_id, characteristics_name)
values (2, 'Производитель'),
       (2, 'Диагональ'),
       (2, 'Матрица'),
       (2, 'Разрешение');

-- Процессоры
insert into products (category_id, product_name, cost)
values (1, 'Intel Core I9 9900', 200000),
       (1, 'AMD Ryzen R7 7700', 150000);

-- Мониторы
insert into products (category_id, product_name, cost)
values (2, 'Samsung SU556270', 260000),
       (2, 'AOC Z215S659', 230000);

-- Процессоры - Intel Core I9 9900
insert into product_characteristic (product_id, characteristic_id, characteristic)
values (1, 1, 'Intel'),
       (1, 2, '8'),
       (1, 3, '1250');

-- Процессоры - AMD Ryzen R7 7700
insert into product_characteristic (product_id, characteristic_id, characteristic)
values (2, 1, 'AMD'),
       (2, 2, '12'),
       (2, 3, 'AM4');

-- Мониторы - Samsung SU556270
insert into product_characteristic (product_id, characteristic_id, characteristic)
values (3, 1, 'Samsung'),
       (3, 4, '27'),
       (3, 5, 'TN'),
       (3, 6, '2560*1440');

-- Мониторы - AOC Z215S659
insert into product_characteristic (product_id, characteristic_id, characteristic)
values (4, 1, 'AOC'),
       (4, 4, '21.5'),
       (4, 5, 'AH-IPS'),
       (4, 6, '1920*1080');



select characteristics_name, characteristic
from product_characteristic
         join characteristics c on product_characteristic.characteristic_id = c.characteristic_id
where product_id = 3;


select distinct product_name
from product_characteristic
         join products p on p.product_id = product_characteristic.product_id
         join characteristics c on c.characteristics_name = 'Производитель' and
                                   c.characteristic_id = product_characteristic.characteristic_id
where characteristic like '%Intel%';

select category_name, count(*)
from characteristics
         join categories c on c.category_id = characteristics.category_id
group by 1;
