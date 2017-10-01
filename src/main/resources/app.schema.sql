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

create table z_timeline (
        id bigserial primary key,
        name text not null,
        description text,
        topic varchar(250) not null,
        post_types varchar(250)[],
        created_at timestamp not null,
        updated_at timestamp,
        index integer not null default 0,
        state varchar(50) not null default 'temp',
        is_active boolean not null default true
);

insert into z_timeline(id, name, topic, post_types, state, created_at)
values(1, 'home', '*', '{steemit-post,twitter-post,bbc-post,youtube-post}', 'created', now());

insert into z_timeline(id, name, topic, post_types, state, created_at)
values(2, 'news', 'news', '{steemit-post,twitter-post,bbc-post,youtube-post}', 'created', now());
