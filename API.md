Marketing
=========
Home page:        [GET]   /
Terms:            [GET]   /terms

User
====
View login page:  [GET]    /user/login
Connection req:   [POST]   /user/login {email, password}
Register page:    [GET]    /user/register
New registration: [POST]   /user/register {email, password, accept_terms}
Forgot password:  [GET]    /user/password
Request password: [POST]   /user/password {email}
Logout:           [GET]    /user/logout

Sites
=====
View site list:   [GET]    /sites
View site detail: [GET]    /sites/:id
Confirm site del: [GET]    /sites/:id/delete
Confirmed:        [DELETE] /sites/:id
Create new site:  [GET]    /sites/create
Confirmed create: [POST]   /sites
Edit site:        [GET]    /sites/:id/edit
Confirm edit:     [PUT]    /sites/:id

Sessions
========
View sessions:    [GET]    /sessions
View session:     [GET]    /sessions/:id
Confirm ses del:  [GET]    /sessions/:id/delete
Confirmed:        [DELETE] /sessions/:id

Events
======
List events:      [GET]    /sessions/:id/events?click=1&...
