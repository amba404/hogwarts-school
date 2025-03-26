alter table student add constraint age_constraint check (age>=18);

alter table student add constraint name_constraint unique (name);

alter table faculty add constraint faculty_constraint unique (name, color);

alter table student alter column age set default 20;