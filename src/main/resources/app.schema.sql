drop schema if exists public cascade;
create schema public;
--
--create table z_profile (
--        id bigserial primary key,
--        title text not null,
--        description text,
--        link text not null,
--        picture text,
--        username varchar(250) not null,
--        likes integer default 0,
--        listing_id bigint not null references z_listing(id),
--
--        -- time
--        indexed_at timestamp not null,
--        last_activity timestamp,
--        last_update timestamp,
--        
--        UNIQUE(username)
--);
--
--create table z_listing (
--        id bigserial primary key,
--        title varchar(250) not null,
--        description text,
--
--        -- time 
--        created_at timestamp default now(),
--        last_update timestamp,
--        last_activity timestamp 
--);
--
--
--create table z_post (
--        id bigserial primary key,
--        description text,
--        cover_html text,
--        time timestamp,
--        type varchar(100) not null,
--        timming varchar(250) not null,
--        picture text not null,
--        blob jsonb,
--        source varchar(150),
--        tags varchar[],
--
--        -- ids
--        facebook_id varchar(250) not null,
--        profile_id bigint not null references z_profile(id),
--        listing_id bigint not null references z_listing(id),
--
--        -- reactions
--        count_likes int default 0 not null,
--        count_love int default 0 not null,
--        count_haha int default 0 not null,
--        count_wow int default 0 not null,
--        count_angry int default 0 not null,
--        count_sad int default 0 not null,
--
--        -- link
--        comment_link text not null,
--        share_link text ,
--        view_link text not null,
--
--        -- time 
--        indexed_at timestamp not null,
--        UNIQUE(facebook_id)
--);
--
--create table z_post_media (
--        id bigserial primary key,
--        title text not null,
--        description text not null,
--        ref varchar(50) not null,
--        type varchar(50) not null,
--        timestamp timestamp not null,
--        data jsonb not null,
--        post_id bigint not null references z_post(id),
--        listing_id bigint not null references z_listing(id),
--        source varchar(150),
--        indexed_at timestamp not null,
--
--        UNIQUE(ref)
--);
--
--insert into z_listing(title, description) 
--values ('general', 'listing for uncategorized posts'), ('breaking-news', 'breaking media social news');

create table z_media (
        id bigserial primary key,
        title text,
        description text,
        type varchar(50) not null,
        timestamp timestamp not null,
        data jsonb not null,
        post_id varchar(150) not null,
        source text,
        cached_at text,
        indexed_at timestamp not null default now()
);

create table z_profile (
        id bigserial primary key,
        username varchar(250) not null,
        name varchar(250),
        picture text,
        data jsonb,

        -- time
        indexed_at timestamp not null now(),
        last_activity timestamp,
        last_update timestamp,
        UNIQUE(username)
);

create table z_channel (
        id bigserial primary key,
        name varchar(250) not null,
        picture text,
        query_conf jsonb,
        curation integer default 0,
        rank integer default -1,
        inteligence jsonb,
        audience varchar(50) not null,
        -- time
        created_at timestamp not null default now(),
        last_update timestamp,
        UNIQUE(name)
);

create table z_collection (
        id bigserial primary key,
        name varchar(250) not null,
        label varchar(250) not null,
        description text,
        audience varchar(50) not null,
        curation decimal(10,2) not null default 0.0,
        update boolean not null default true,
        display boolean not null default false,
        created_at timestamp not null default now(),
        last_update timestamp,
        channel_id bigint references z_channel(id),
        UNIQUE(name)
);

insert into z_collection(id, name, display, label, audience, curation)
values(1, 't/core', false, 'internal collection', 'private', 1);

create table z_user (
        id bigserial primary key,
        name varchar(250) not null,
        email text not null,
        picture text,
        password_hash text not null,
        location varchar(250),
        lang varchar(250),
        created_at timestamp not null default now(),
        UNIQUE(email)
);

