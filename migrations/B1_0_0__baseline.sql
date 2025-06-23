create schema test;

create table test.t1 (
	id bigserial primary key,
	message_type text not null,
	message_value text not null
);
