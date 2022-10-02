![](Aspose.Words.a2caee26-2657-4ae3-8a06-ad76c62a16fa.001.png)
<img src="https://webassets.telerikacademy.com/images/default-source/logos/telerik-academy.svg" alt="logo" width="300px" style="margin-top: 20px;"/>

# Virtual Teacher

![](Aspose.Words.a2caee26-2657-4ae3-8a06-ad76c62a16fa.002.png)![](Aspose.Words.a2caee26-2657-4ae3-8a06-ad76c62a16fa.003.png)


# Project Description
**Virtual Teacher** is an online platform for tutoring. Users will be able to register as either a teacher or a student. 

**Students** must be able to enroll for video courses, watch the available video lectures. After finishing each video within a course, students will be asked to submit an assignment, which will be graded by the teacher. After a successful completion of a course, the students must be able to rate it.

**Teachers** should be able to create courses and upload video lectures with assignments. Each assignment will be graded individually after submission from the students. Users can become teachers only after approval from administrator. 
# Functional Requirements
## Entities
- Each **user** (teacher or student) must have a profile picture, first and last name, email, and a password.
  - The email will serve as their username so it must be a valid email and unique in the application.
  - The password must be at least 8 symbols and should contain capital letter, digit, and special symbol.
  - The first and last names must be between 2 and 20 characters long.
- **Courses** must have a title, a topic, a description, and a list of lectures. They should also have a starting date.
  - The title is a string between 5 and 50 characters long.
  - The topic is one of a predefined range like Science, History, Literature, and others.
  - The description is optional and can be at most 1000 symbols in length.
  - The starting date, if set, makes the course visible to the students but they cannot enroll before the said date.
- Each **lecture** must have, a title, a description, a video, and an assignment.
  - The title is a string between 5 and 50 characters long.
  - The description is optional and can be at most 1000 symbols in length.
  - The video must be embedded (for example from YouTube).
  - The assignment must be a text file, saved to the file system, or optionally, could be uploaded to a cloud file hosting service.
## Public Section
The public part must be visible without authentication. 

Anonymous users must be able to register as students or teachers.

The anonymous users must also be able to browse the catalog of available courses. The user must be able to filter the catalog by course name, course topic, teacher, and rating, as well as to sort the catalog by name and/or rating.

Anonymous users must not be able to enroll for a course.
## Private Part (Students)
Must be accessible only if the student is authenticated.
### Users
Users **must** have private section (profile page).

Users must be able to:

- View and edit their profile (names and picture).
- Change their password.
- Enroll on courses.
- See their enrolled and completed courses.

Users could be able to:

- Take notes in plain text for each lecture.
### Courses
- The videos in a course must be accessible only after enrollment.
- Students must submit their work as a text file (txt, doc, docx, etc.). 
- Students must be able to see the grade they’ve received for the assignment and their average grade for the course (formed by the average of all assignments grades).
- After receiving a grade for their last assignment for the course, if their average grade is above the passing grade, defined on per course basis, they should be able to leave a rating for the course
## Private Part (Teachers) 
Teachers must have access to all the functionality that students do.

However, after they are approved by an administrator, a course administration page must be accessible to them. On it they must be able to modify courses and have a section where they can search for students.

Courses must be either drafts or published. Draft courses are visible to the teachers, but not to the students. Once the teacher has prepared the course, they must be able to publish it and it becomes available to all students.
Courses could have a comments page, where students can comment on the lectures and ask questions.
## Administration Part
Administrators must have access to all the functionality that teachers do.

Administrators must not be registered though the ordinary registration process. They can be given administrator rights from the UI by other administrators only.

They must be able to modify/delete courses and users, approve administrators and teachers.
## Optional features
**Email Verification** – In order for the registration to be completed, the user must verify their email by clicking on a link sent to their email by the application. Before verifying their email, users cannot make transactions.

**Refer a Friend** – A user can enter the email of people, not yet registered for the application, and invite them to register. The application sends to that email a registration link.

**Attendance tracker** – some courses can have required attendance. Each lecture from the course should have a single attendance tracker. In the tracker, the teacher marks the absent students. The teacher should only have access to the admin part of the sessions and the enrolled students should only see their attendance record.

**Enrollment confirmation** – the user can receive an email confirmation after enrolling in a course with all essential information regarding the course.

**Graduation Certificate** – After finishing a course, the user can download and/or receive an email with the certificate in PDF format.

**Advanced courses** – some courses may only be available if the student has completed other previous courses. There can be multiple levels of course hidden behind these requirements. For example, beginner - it’s available to all; intermediate – only available if the student has completed at least 1 course; advanced – student has completed at least 3 courses prior. You are free to add more levels if you wish.

**Student groups** – each course should have groups with participants enrolled in the course. Students outside the course must not be able to be added to the groups. The group will enable the Teacher to give out group tasks. If one of the students uploads a solution to the task it counts as if everyone uploaded a solution. The teacher needs to grade only 1 upload per group. The grade is the same for all students of that group.

**Course Rating** – each course can be rated from 1 to 5 from the students. The comment on the rating is mandatory to the rating and it should be a minimum of 75 symbols. 

**Entrance Exam** – each course can have an entrance exam minimum grade as an enrollment requirement. The teacher should be able to make it a quiz of questions. There should be the option of MCQ (multiple-choice-question), MSQ (multiple-select-question), or OEQ (open-ended-questions) - the OEQ should be graded by the teacher.

The Entrance Exam can have different durations – the teacher should be able to set start and end time. The exam should close automatically after the time expires.

**Tutoring** – the teacher should be able to show in their profiles if they are available for private lessons (tutoring sessions) and when.

**Additional Resources** –** the teacher should be able to offer under each course additional resources which are accessible only after enrollment. The enrolled students should be able to request additional resources by leaving a request (min. 100 symbols). The teacher should be able to mark the request as resolved, not relevant, or work in progress. He can also assign the request to another student.

**Assistant Teacher** –** a student** can become an assistant teacher to a specific teacher. He can then edit all courses content of the teacher. The assistant teacher can create only drafts of new courses. Each course can then have three phases – draft, to be reviewed, and published. The teacher can review it and either publish it or return it in draft for corrections to be done. If the teacher returns it in the draft a note of a minimum of 75 symbols is required.

**Course Lessons Availability** – each lesson of a course can be made available after a specific date or after a specific action (submit of homework). The course can also be made available after minimum participants have enrolled – in this case the course should have an explanation and enrolled participants count. 

**Easter eggs** – Creativity is always welcome and appreciated. Find a way to add something fun and/or interesting, maybe an Easter egg or two to you project to add some variety.
## REST API
To provide other developers with your service, you need to develop a REST API. It should leverage HTTP as a transport protocol and clear text JSON for the request and response payloads.

A great API is nothing without a great documentation. The documentation holds the information that is required to successfully consume and integrate with an API. You must use [Swagger](https://swagger.io/) to document yours.

The REST API provides the following capabilities: 

1. Users
   1. CRUD operations (must)
   1. Enroll to courses (must)
   1. Search by first, last name or email (must)
1. Courses
   1. CRUD operations (must)
   1. Rate (if implemented) (must)
1. Lectures
   1. CRUD operations (must)
   1. Submit assignment file (must) 
   1. Add/Edit user’s notes (if implemented)
# Technical Requirements
## General
- Follow [OOP](https://en.wikipedia.org/wiki/Object-oriented_programming) principles when coding
- Follow [KISS](https://en.wikipedia.org/wiki/KISS_principle), [SOLID](https://en.wikipedia.org/wiki/SOLID), [DRY](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself) principles when coding
- Follow REST API design [best practices](https://blog.florimondmanca.com/restful-api-design-13-best-practices-to-make-your-users-happy) when designing the REST API (see Appendix)
- Use tiered project structure (separate the application in layers)
- The service layer (i.e., "business" functionality) must have at least 80% unit test code coverage
- Follow [BDD](https://en.wikipedia.org/wiki/Behavior-driven_development) when writing unit tests
- You should implement proper exception handling and propagation
- Try to think ahead. When developing something, think – “How hard would it be to change/modify this later?”

## Database
The data of the application must be stored in a relational database. You need to identify the core domain objects and model their relationships accordingly. Database structure should avoid data duplication and empty data (normalize your database).

Your repository must include two scripts – one to create the database and one to fill it with data.
## Git
Commits in the GitLab repository should give a good overview of how the project was developed, which features were created first and the people who contributed. Contributions from all team members must be evident through the git commit history! The repository must contain the complete application source code and any scripts (database scripts, for example).

Provide a link to a GitLab repository with the following information in the README.md file:

- Project description
- Link to the Swagger documentation (must)
- Link to the hosted project (if hosted online)
- Instructions how to setup and run the project locally 
- Images of the database relations (must)

## Optional Requirements
Besides all requirements marked as should and could, here are some more *optional* requirements:

- Use a branching while working with Git.
- Integrate your app with a Continuous Integration server (e.g., GitLab’s own) and configure your unit tests to run on each commit to the master branch.
- Host your application’s backend in a public hosting provider of your choice (e.g., AWS, Azure, Heroku).
# Teamwork Guidelines
Please see the Teamwork Guidelines document. 
# Appendix
- [Guidelines for designing good REST API](https://blog.florimondmanca.com/restful-api-design-13-best-practices-to-make-your-users-happy)
- [](https://blog.florimondmanca.com/restful-api-design-13-best-practices-to-make-your-users-happy)[Guidelines for URL encoding](http://www.talisman.org/~erlkonig/misc/lunatech%5Ewhat-every-webdev-must-know-about-url-encoding/)
- [](http://www.talisman.org/~erlkonig/misc/lunatech%5Ewhat-every-webdev-must-know-about-url-encoding/)[Always prefer constructor injection](https://www.vojtechruzicka.com/field-dependency-injection-considered-harmful/)
- [](https://www.vojtechruzicka.com/field-dependency-injection-considered-harmful/)[Git commits - an effective style guide](https://dev.to/pavlosisaris/git-commits-an-effective-style-guide-2kkn)
- [](https://dev.to/pavlosisaris/git-commits-an-effective-style-guide-2kkn)[How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/)
# Legend
- Must – Implement these first.
- Should – if you have time left, try to implement these.
- Could – only if you are ready with everything else give these a go.
