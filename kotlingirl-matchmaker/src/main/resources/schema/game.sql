begin;

drop schema if exists game cascade;
create schema game;

drop table if exists game.playerData;
create table game.player (
  id                serial             not null,
  login             varchar(20) unique not null,
  password          varchar(16)        not null,
  rating            integer            not null,
  logged            boolean            not null,
  modified_at       date               not null,
  registration_date date               not null,
  games_played      integer            not null,
  games_won         integer            not null,
  current_game      Serial                     ,

  constraint player_data_game_session_data_id_fk foreign key (current_game) references game_session_data,
  primary key (id)
);

drop table if exists game.sessionData;
create table game.sessionData (
  id        serial       not null,
  game_type varchar(20)  not null,
  primary key (id)
);

commit;

