select s.name as student_name,
       s.age  as student_age,
       f.name as faculty_name
from student s
         inner join faculty f on s.faculty_id = f.id;

select s.*
from student s
         inner join avatar a on s.id = a.student_id;
