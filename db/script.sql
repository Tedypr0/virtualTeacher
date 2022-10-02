create table assignments
(
    assignment_id  int auto_increment
        primary key,
    assignment_url text                 not null,
    is_deleted     tinyint(1) default 0 not null,
    constraint assignments_assignment_id_uindex
        unique (assignment_id)
);

create table roles
(
    role_id   int auto_increment
        primary key,
    role_name varchar(20) not null,
    constraint roles_role_name_uindex
        unique (role_name)
);

create table topics
(
    topic_id        int auto_increment
        primary key,
    topic_name      varchar(100)  not null,
    topic_photo_url varchar(1000) not null,
    constraint topics_topic_id_uindex
        unique (topic_id)
);

create table courses
(
    course_id     int auto_increment
        primary key,
    title         varchar(50)          not null,
    topic_id      int                  not null,
    is_deleted    tinyint(1) default 0 not null,
    is_draft      tinyint(1) default 0 null,
    is_completed  tinyint(1) default 0 not null,
    user_id       int                  not null,
    end_date      date                 not null,
    starting_date date                 not null,
    avg_rating    double               not null,
    constraint courses_course_id_uindex
        unique (course_id),
    constraint courses_title_uindex
        unique (title),
    constraint courses_topics_topic_id_fk
        foreign key (topic_id) references topics (topic_id)
);

create table courses_descriptions
(
    course_description_id int auto_increment
        primary key,
    course_id             int           not null,
    description           varchar(1000) not null,
    is_deleted            tinyint(1)    not null,
    constraint courses_descriptions_course_description_id_uindex
        unique (course_description_id),
    constraint courses_descriptions_courses_course_id_fk
        foreign key (course_id) references courses (course_id)
);

create table users
(
    user_id       int auto_increment
        primary key,
    first_name    varchar(20)  not null,
    last_name     varchar(20)  not null,
    email         varchar(100) not null,
    password      varchar(50)  not null,
    role_id       int          not null,
    is_active     tinyint(1)   not null,
    is_deleted    tinyint(1)   not null,
    creation_date datetime     null,
    constraint users_email_uindex
        unique (email),
    constraint users_roles_role_id_fk
        foreign key (role_id) references roles (role_id)
);

create table courses_comments
(
    course_comment_id int auto_increment
        primary key,
    user_id           int                  not null,
    course_id         int                  not null,
    content           varchar(1000)        not null,
    creation_date     datetime             not null,
    is_deleted        tinyint(1) default 0 not null,
    constraint courses_comments_course_comment_id_uindex
        unique (course_comment_id),
    constraint courses_comments_courses_course_id_fk
        foreign key (course_id) references courses (course_id),
    constraint courses_comments_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table ratings
(
    rating_id     int auto_increment
        primary key,
    rating_score  int           null,
    course_id     int           not null,
    user_id       int           not null,
    review        varchar(1000) not null,
    creation_date date          not null,
    constraint ratings_rating_id_uindex
        unique (rating_id),
    constraint ratings_courses_course_id_fk
        foreign key (course_id) references courses (course_id),
    constraint ratings_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table teacher_applications
(
    id                  int auto_increment
        primary key,
    user_id             int           not null,
    motivational_letter varchar(1000) not null,
    constraint teacher_applications_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table users_courses
(
    user_course_id int auto_increment
        primary key,
    user_id        int not null,
    course_id      int not null,
    constraint users_courses_user_course_id_uindex
        unique (user_course_id),
    constraint users_courses_courses_course_id_fk
        foreign key (course_id) references courses (course_id),
    constraint users_courses_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table videos
(
    video_id   int auto_increment
        primary key,
    video_url  text                 null,
    is_deleted tinyint(1) default 0 not null,
    constraint videos_video_id_uindex
        unique (video_id)
);

create table lectures
(
    lecture_id         int auto_increment
        primary key,
    title              varchar(50)          not null,
    video_id           int                  not null,
    is_deleted         tinyint(1)           not null,
    user_id            int                  not null,
    is_added_to_course tinyint(1) default 0 not null,
    constraint lectures_lecture_id_uindex
        unique (lecture_id),
    constraint lectures_users_user_id_fk
        foreign key (user_id) references users (user_id),
    constraint lectures_videos_video_id_fk
        foreign key (video_id) references videos (video_id)
);

create table courses_lectures
(
    course_lecture_id int auto_increment
        primary key,
    course_id         int not null,
    lecture_id        int not null,
    constraint courses_lectures_course_lecture_id_uindex
        unique (course_lecture_id),
    constraint courses_lectures_courses_course_id_fk
        foreign key (course_id) references courses (course_id),
    constraint courses_lectures_lectures_lecture_id_fk
        foreign key (lecture_id) references lectures (lecture_id)
);

create table homeworks
(
    id            int auto_increment
        primary key,
    homework_name varchar(50) null,
    lecture_id    int         not null,
    user_id       int         not null,
    grade         double      not null,
    is_deleted    tinyint(1)  not null,
    constraint homeworks_lectures_lecture_id_fk
        foreign key (lecture_id) references lectures (lecture_id),
    constraint homeworks_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table lectures_comments
(
    lecture_comment_id int auto_increment
        primary key,
    lecture_id         int                  not null,
    user_id            int                  not null,
    content            varchar(1000)        not null,
    creation_date      datetime             not null,
    is_deleted         tinyint(1) default 0 not null,
    constraint lectures_comments_lecture_comment_id_uindex
        unique (lecture_comment_id),
    constraint lectures_comments_lectures_lecture_id_fk
        foreign key (lecture_id) references lectures (lecture_id),
    constraint lectures_comments_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table lectures_descriptions
(
    lecture_description_id int auto_increment
        primary key,
    description            varchar(1000)        not null,
    lecture_id             int                  not null,
    is_deleted             tinyint(1) default 0 not null,
    constraint lectures_descriptions_lecture_description_id_uindex
        unique (lecture_description_id),
    constraint lectures_descriptions_lectures_lecture_id_fk
        foreign key (lecture_id) references lectures (lecture_id)
);

create table notes
(
    note_id    int auto_increment
        primary key,
    user_id    int                  not null,
    note       text                 not null,
    lecture_id int                  not null,
    is_deleted tinyint(1) default 0 not null,
    constraint notes_note_id_uindex
        unique (note_id),
    constraint notes_lectures_lecture_id_fk
        foreign key (lecture_id) references lectures (lecture_id),
    constraint notes_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table users_grades
(
    user_grade_id int auto_increment
        primary key,
    user_id       int          not null,
    lecture_id    int          not null,
    homework_url  varchar(100) not null,
    grade         int          not null,
    constraint users_grades_user_grade_id_uindex
        unique (user_grade_id),
    constraint users_grades_lectures_lecture_id_fk
        foreign key (lecture_id) references lectures (lecture_id),
    constraint users_grades_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

