CREATE TABLE public.post (
	id int4 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	body varchar(255) NULL,
	title varchar(255) NULL,
	user_id int4 NULL,
	"version" int4 NULL
);
