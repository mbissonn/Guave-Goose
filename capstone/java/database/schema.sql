BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS forums;
DROP TABLE IF EXISTS user_forum;


CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
    active boolean default false,
    answer_1 character varying(2048) NULL,
    answer_2 character varying(2048) NULL,
    answer_3 character varying(2048) NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE comments
(
    comment_id SERIAL,
    user_id integer NOT NULL,
    comment_body text COLLATE pg_catalog."default" NOT NULL,
    post_id integer NOT NULL,
    date_created date NOT NULL,
    CONSTRAINT comments_pkey PRIMARY KEY (comment_id)
);

CREATE TABLE posts
(
    post_id SERIAL,
    post_title character varying(50) COLLATE pg_catalog."default" NOT NULL,
    image_source character varying(2048) NUll, 
    user_id integer NOT NULL,
    up_votes integer NOT NULL default 0,
    down_votes integer NOT NULL default 0,
    date_posted date NOT NULL,
    post_body text COLLATE pg_catalog."default" NOT NULL,
    forum_id integer NOT NULL,
    CONSTRAINT posts_pkey PRIMARY KEY (post_id)
);

CREATE TABLE forums
(
    forum_id SERIAL,
    forum_image character varying(2048) NULL,
    forum_description text NOT NULL DEFAULT 'DESCIPTION GOES HERE',
    forum_title character varying(50) COLLATE pg_catalog."default" NOT NULL,
    original_poster_user_id integer NOT NULL,
    date_created date NOT NULL,
    question_1 character varying(2048) DEFAULT 'Why do you want to join this forum?' NOT NULL,
    question_2 character varying(2048) DEFAULT 'How did you find this forum?' NOT NULL,
    question_3 character varying(2048) DEFAULT 'What content do you plan on posting?' NOT NULL,
    CONSTRAINT forums_pkey PRIMARY KEY (forum_id)
);


CREATE TABLE user_forum
(
    forum_id integer NOT NULL,
    user_id integer NOT NULL,
    forum_role_id integer DEFAULT 0 NOT NULL CHECK(forum_role_id BETWEEN 0 AND 2),
    is_favorited boolean DEFAULT false NOT NUll,
    answer_1 character varying(2048) NULL,
    answer_2 character varying(2048) NULL,
    answer_3 character varying(2048) NULL,
	PRIMARY KEY (forum_id,user_id)
);

CREATE TABLE forum_roles
(
    forum_role_id integer NOT NULL,
    forum_role character varying(30) NOT NULL,
    PRIMARY KEY (forum_role_id)
);

CREATE TABLE user_post
(
    user_id integer NOT NULL,
    post_id integer NOT NULL,
    vote_value integer NOT NULL DEFAULT 0 CHECK(vote_value BETWEEN -1 AND 1),
    CONSTRAINT user_post_pk PRIMARY KEY (user_id, post_id)
);



ALTER TABLE comments add foreign key(user_id) references users(user_id);
ALTER TABLE comments add foreign key(post_id) references posts(post_id);

ALTER TABLE forums add foreign key(original_poster_user_id) references users(user_id);

ALTER TABLE posts add foreign key(user_id) references users(user_id);
ALTER TABLE posts add foreign key(forum_id) references forums(forum_id);

ALTER TABLE user_forum add foreign key(user_id) references users(user_id);
ALTER TABLE user_forum add foreign key(forum_id) references forums(forum_id);
ALTER TABLE user_forum add foreign key(forum_role_id) references forum_roles(forum_role_id);

ALTER TABLE user_post add foreign key(user_id) references users(user_id);
ALTER TABLE user_post add foreign key(post_id) references posts(post_id);


COMMIT TRANSACTION;
