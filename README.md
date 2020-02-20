# sphynx

Simple tool to allow a website administrator to understand the users interactions with their website.

## Git repository

https://github.com/nathaliebize/sphynx

## Website

https://www.sphynx.dev

## Why did I build sphynx?

I wanted to experiment with multi-tiered architectures. I choose popular frameworks and libraries to be familiar with in demand corporate choices.
My project had a double specification. I wanted a web application that involves technical requirements such as security management, data collection, processing and storage. However, the design would be simple enough to respect realistic ressources of time development, space and cost constraints.

## Structure description

The user interface is written in HTML and CSS that are rendered by a Thymeleaf engine. 
On the server-side, a Spring boot application runs on a Tomcat container. It is written in Java and used a MVC pattern.
Hibernate is used to persist data in a Postgres data base.
