CREATE TABLE public.post (
	id int4 NOT NULL,
	user_id int4 NOT NULL,
	title varchar(250) NOT NULL,
	body text NOT NULL,
	"version" int4 NULL,
	CONSTRAINT post_pkey PRIMARY KEY (id)
);




