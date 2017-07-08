drop schema if exists public cascade;
create schema public;

create table z_profile (
        id bigserial primary key,
        title text not null,
        description text,
        link text not null,
        picture text,
        username varchar(250) not null,
        likes integer default 0,
        listing_id bigint not null references z_listing(id),

        -- time
        indexed_at timestamp not null,
        last_activity timestamp,
        last_update timestamp,
        
        UNIQUE(username)
);

create table z_listing (
        id bigserial primary key,
        title varchar(250) not null,
        description text,

        -- time 
        created_at timestamp default now(),
        last_update timestamp,
        last_activity timestamp 
);


create table z_post (
        id bigserial primary key,
        description text,
        cover_html text,
        time timestamp,
        type varchar(100) not null,
        timming varchar(250) not null,
        picture text not null,

        -- ids
        facebook_id varchar(250) not null,
        profile_id bigint not null references z_profile(id),
        listing_id bigint not null references z_listing(id),

        -- reactions
        count_likes int default 0 not null,
        count_love int default 0 not null,
        count_haha int default 0 not null,
        count_wow int default 0 not null,
        count_angry int default 0 not null,
        count_sad int default 0 not null,

        -- link
        comment_link text not null,
        share_link text ,
        view_link text not null,

        -- time 
        indexed_at timestamp not null,
        UNIQUE(facebook_id)
);

create table z_post_media (
        id bigserial primary key,
        title text not null,
        description text not null,
        type varchar(50) not null,
        timestamp timestamp not null,
        data jsonb not null,
        post_id bigint not null references z_post(id),
        indexed_at timestamp not null
);

insert into z_listing(title, description) 
values ('general', 'listing for uncategorized posts'), ('breaking-news', 'breaking media social news');

