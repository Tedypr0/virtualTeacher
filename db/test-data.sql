insert into roles
values (1, 'Student'),
       (2, 'Teacher'),
       (3, 'Admin');

insert into users
values (1, 'Neda', 'Marinova', 'neda@neda.com', '123456789Tt$', 2, 1, 0, now()),
       (2, 'Teodor', 'Zhelyazkov', 'teo@teo.com', '123456789Tt$', 1, 1, 0, now()),
       (3, 'Mehmed', 'Ukov', 'mehmed@mehmed.com', '123456789Tt$', 1, 1, 0, now()),
       (4, 'Tedyy', 'Tedyyy', 'testtedy@abv.bg', '123456789Tt$', 1, 1, 0, now()),
       (5, 'Admin', 'Admin', 'admin@abv.bg', '123456789Tt$', 3, 1, 0, now()),
       (6, 'Teacher', 'Teacher', 'teacher@abv.bg', '123456789Tt$', 2, 1, 0, now()),
       (7, 'Student', 'Student', 'student@abv.bg', '123456789Tt$', 1, 1, 0, now());


insert into videos
values (1, 'https://www.youtube.com/embed/GoXwIVyNvX0', 0),
       (2, 'https://www.youtube.com/embed/xzjZy-dHHLw', 0),
       (3, 'https://www.youtube.com/embed/cCgOESMQe44', 0),
       (4, 'https://www.youtube.com/embed/aqcJsKdjjvU', 0),
       (5, 'https://www.youtube.com/embed/RWyY3n9nTf8', 0),
       (6, 'https://www.youtube.com/embed/bDPRWJdVzfs', 0),
       (7, 'https://www.youtube.com/embed/9CGY0s-uCUE', 0),
       (8, 'https://www.youtube.com/embed/pfRTAI6Anhk', 0),
       (9, 'https://www.youtube.com/embed/YoSghxkxBVQ', 0),
       (10, 'https://www.youtube.com/embed/jDif8oXM7mI', 0),
       (11, 'https://www.youtube.com/embed/h7piyWnQbZA', 0),
       (12, 'https://www.youtube.com/embed/cU94So54cr8', 0),
       (13, 'https://www.youtube.com/embed/j1RjRwQPvzY', 0),
       (14, 'https://www.youtube.com/embed/pDTNUS8mgc0', 0),
       (15, 'https://www.youtube.com/embed/k57PtwQKFoA', 0);

insert into lectures
values (1, 'Java Basics Introduction', 1, 0, 1, 1),
       (2, 'Arrays', 2, 0, 1, 1),
       (3, 'Methods', 3, 0, 1, 1),
       (4, 'Debugging', 4, 0, 1, 1),
       (5, 'Angles', 5, 0, 6, 1),
       (6, 'Sine', 6, 0, 6, 1),
       (7, 'Cosine', 7, 0, 6, 1),
       (8, 'Tangent', 8, 0, 6, 1),
       (9, 'Inheritance and Polymorphism', 9, 0, 1, 1),
       (10, 'Abstract Classes and Interfaces', 10, 0, 1, 1),
       (11, 'Collections & Generics', 11, 0, 1, 1),
       (12, 'Encapsulation', 12, 0, 1, 1),
       (13, 'Algorithms Analysis & Linear Data Structures', 13, 0, 6, 1),
       (14, 'Set, Map and Hash Table', 14, 0, 6, 1),
       (15, 'Searching and Sorting', 15, 0, 6, 1);



insert into lectures_descriptions
values (1, 'In this lecture you will be introduced to the world of Java', 1, 0),
       (2, 'We will learn how to use methods in Java', 3, 0),
       (3, 'Your code is not working? Let us teach you how to debug it', 4, 0),
       (4, 'Today we will learn what angles are and how to use them', 5, 0),
       (6, 'Let us teach you how to use abstraction and interfaces', 10, 0),
       (7, 'Time to introduce Collections and Generics', 11, 0);

insert into lectures_comments
values (1, 1, 1, 'This lecture is awesome', now(), 0),
       (2, 1, 2, 'I am not impressed but it was good', now(), 0),
       (3, 2, 3, 'Great', now(), 0),
       (4, 2, 4, 'It was really interesting', now(), 0),
       (5, 2, 1, 'Somebody else who could not do the homework?', now(), 0),
       (6, 3, 2, 'Hey, I would like to consult on this with somebody', now(), 0),
       (7, 4, 3, 'Perfect', now(), 0),
       (8, 4, 4, 'Great lecture, a lot of useful information', now(), 0),
       (9, 4, 1, 'It is so easy to study when you watch videos', now(), 0),
       (10, 4, 2, 'Please help, I cannot submit my homework', now(), 0),
       (11, 4, 3, 'I am here to meet new people', now(), 0),
       (12, 4, 4, 'Awesome', now(), 0);

insert into topics
values (1, 'Mathematics', 'https://www.filepicker.io/api/file/kIJhurteRNWyd3qvUo22'),
       (2, 'Java',
        'https://images.idgesg.net/images/article/2019/05/java_binary_code_gears_programming_coding_development_by_bluebay2014_gettyimages-1040871468_2400x1600-100795798-large.jpg?auto=webp&quality=85,70'),
       (3, 'C', 'https://qubit.institute/wp-content/uploads/2022/05/C-Programming.jpg'),
       (4, 'C++',
        'https://bs-uploads.toptal.io/blackfish-uploads/components/blog_post_page/content/cover_image_file/cover_image/687167/retina_1708x683_cover-0828_AfterAllTheseYearstheWorldisStillPoweredbyCProgramming_Razvan_Newsletter-2b9ea38294bb08c5aea1f0c1cb06732f.png'),
       (5, 'C#', 'https://static.skillshare.com/uploads/discussion/tmp/b8ba300b.png'),
       (6, 'Python', 'https://www.trio.dev/hubfs/Imported_Blog_Media/python_logo.jpg'),
       (7, 'Javascript', 'https://alfa-cyber.com/wp-content/uploads/2021/06/What-is-JavaScript.jpg'),
       (8, 'CSS', 'https://repository-images.githubusercontent.com/428080046/8073eeb4-3a1a-4066-ac50-4bf75259756c'),
       (9, 'HTML', 'https://www.codingdiv.com/wp-content/uploads/2021/02/HTML.png'),
       (10, 'Spring Framework', 'https://spring.io/images/OG-Spring.png'),
       (11, 'Artificial Intelligence',
        'https://i0.wp.com/aanworld.com/wp-content/uploads/2022/07/image_search_1657688308929.jpg'),
       (12, 'Machine Learning',
        'https://www.simplilearn.com/ice9/free_resources_article_thumb/Deep-Learning-vs-Machine-Learning.jpg');

insert into courses
values (1, 'Trigonometry', 1, 0, 0, 0, 6, '2022-10-17', '2022-09-01', 4),
       (2, 'Java Basics', 2, 0, 0, 0, 1, '2022-11-19', '2022-10-16', 4.3),
       (3, 'Java Object-Oriented Programming', 2, 0, 0, 0, 1, '2022-11-13', '2022-09-13', 2.2),
       (4, 'Java Advanced', 2, 0, 0, 0, 6, '2022-12-12', '2022-09-13', 5),
       (5, 'Javascript for Beginners', 6, 0, 0, 0, 1, '2022-10-17', '2022-09-01', 4),
       (6, 'Javascript Advanced', 6, 0, 0, 0, 1, '2022-11-19', '2022-10-16', 4.3),
       (7, 'Learn C# Programming', 4, 0, 0, 0, 6, '2022-11-13', '2022-09-13', 2.2),
       (8, 'C# DSA', 4, 0, 0, 0, 6, '2022-12-13', '2022-10-13', 4),
       (9, 'Python Beyond the Basics', 5, 0, 0, 0, 1, '2022-12-13', '2022-10-13', 5),
       (10, 'Python for Beginners', 5, 0, 0, 0, 1, '2022-10-13', '2022-09-13', 5),
       (11, 'C++ Advanced', 3, 0, 0, 0, 6, '2022-12-13', '2022-09-13', 3),
       (12, 'C++ OOP', 3, 0, 0, 0, 6, '2023-01-13', '2022-12-13', 5),
       (13, 'Java Spring Security', 1, 0, 0, 0, 1, '2022-12-13', '2022-10-13', 4),
       (14, 'Alpha Java Program', 1, 0, 0, 0, 1, '2023-04-13', '2022-10-13', 4),
       (15, 'Introduction to Machine Learning', 11, 0, 0, 0, 6, '2022-11-13', '2022-10-13', 5),
       (16, 'AI Masterclass', 10, 0, 0, 0, 6, '2022-11-13', '2022-10-13', 5),
       (17, 'Advanced C Programming', 2, 0, 0, 0, 1, '2022-12-13', '2022-10-13', 4);



insert into courses_lectures
values (1, 1, 5),
       (2, 1, 6),
       (3, 1, 7),
       (4, 1, 8),
       (5, 2, 2),
       (6, 2, 3),
       (7, 2, 4),
       (8, 3, 9),
       (9, 3, 10),
       (10, 3, 11),
       (11, 3, 12),
       (12, 4, 13),
       (13, 4, 14),
       (14, 4, 15),
       (15, 2, 1);

insert into courses_comments
values (1, 1, 1, 'This course is awesome', now(), 0),
       (2, 2, 2, 'I am not impressed but it was good', now(), 0),
       (3, 3, 3, 'Great', now(), 0),
       (4, 4, 4, 'It was really interesting', now(), 0),
       (5, 1, 1, 'Somebody else who will take the course?', now(), 0),
       (6, 2, 2, 'Hey, I would like to ask something about the course', now(), 0),
       (7, 3, 3, 'Perfect', now(), 0),
       (8, 4, 4, 'Great course, a lot of useful information', now(), 0),
       (9, 4, 1, 'It is so easy to study when you watch videos', now(), 0),
       (10, 1, 2, 'Please help, I cannot apply', now(), 0),
       (11, 3, 3, 'I am here to meet new people', now(), 0),
       (12, 2, 4, 'Awesome', now(), 0);

insert into courses_descriptions
values (1, 1, 'If you are interested in Mathematics, do not hesitate to apply for the Trigonometry course', 0),
       (2, 2, 'Take a dive in the Java world and start learning how to code', 0);

insert into notes
values (1, 7,
        'Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible. It is a general-purpose programming language intended to let programmers write once, run anywhere (WORA)',
        1, 0),
       (2, 7,
        'Encapsulation is one of the four fundamental OOP concepts. The other three are inheritance, polymorphism and abstraction. Encapsulation in Java is a mechanism of wrapping the data (variables) and code acting on the data (methods) together as a single unit.',
        12, 0);

insert into ratings
values (1, 5, 1, 2,
        'The course was very comprehensive and easy to understand. The instructors made sure that they are giving the information in a way that won''t make me confused. Thank you so much for this great course!',
        '2022-08-13'),
       (2, 6, 2, 3,
        'This course has taught me a lot of techniques in searching information for my  academic researches. Thank you to all people behind this, especially to the professors in this course!',
        '2022-07-05'),
       (3, 4, 3, 4,
        'Extremely valuable for researching techniques. Teachers were outstanding. Lectures are to the point without drag-on. Many thanks for the quality of your efforts!',
        '2022-09-01'),
       (4, 5, 3, 7,
        'The course is organised in the best way to support learners in academic information seeking. After the course, I was able to identify my faults which I have done before in conducting research. Thanks to all the amazing instructors.',
        '2022-06-13'),
       (5, 6, 2, 7,
        'This course is very thorough and detailed. As an IT specialist, I have had no exposure to formal research and needed to understand the process. Now I can clearly  and confidently say that I can perform good research and obtain formal information and data on any topic, as opposed to just surfing the internet for genuine knowledge. Great course, well done to the instructors.',
        '2022-04-12'),
       (6, 4, 1, 4,
        'The course is structured with contents meeting the objectives of the course. Special credit to all the teachers, I left as if they were explaining it on the face. The language was very clear and understandable.',
        '2022-02-10'),
       (7, 5, 3, 3,
        'This course is truly one of a kind. Am a matured student planning to go back to study. Trying to navigate and prepare myself for the academic world.',
        '2022-09-13'),
       (8, 6, 4, 2,
        'I loved this course, it was very objective and direct. I''m sure it will be very useful in my life as a student and as a professional.',
        '2022-09-02'),
       (9, 5, 2, 2,
        'I adore everything in this course.  Each explanation works for me and it loads of great knowledge and know-how across defining information needs.',
        '2022-06-04'),
       (10, 6, 1, 3,
        'Amazing course! Incredibly simple, fast-paced and provided a lot of useful information for someone like me, who never was "initiated" in the university academic rigor. Now I can say I have a notion of the principles behind a quality work. Thanks a lot!',
        '2022-08-22'),
       (11, 5, 4, 3,
        'I love this course. It helps me with my teaching and personal development. Besides, I know several reference management tools from this course. Storing the documents is really complicated and makes me in a mess. Thank you for your tutoring and sharing.',
        '2022-07-23');

insert into teacher_applications
values (1, 2,
        'I apply for a teacher as I have experience in this field. I have worked as a teacher in a school for more than 15 years and I would like to become a part of your team.'),
       (2, 3, 'I do not have any experience as a teacher yet, but I am a fast learner and a team player.');

insert into users_courses
values (1, 2, 1),
       (2, 3, 2),
       (3, 7, 3);



