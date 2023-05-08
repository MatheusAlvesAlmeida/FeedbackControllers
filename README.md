# RabbitMQ Client with Feedback Controllers
This is a project that implements a RabbitMQ client with feedback controllers. The goal of this project is to control the prefetch count variable in RabbitMQ using different feedback controllers.

The project was designed by the professor Nelson Souto, from CIN - UFPE. 

# Overview

Feedback systems are used in control engineering to regulate and maintain a process or system at a desired state. These systems measure the output of a process or system, compare it with the desired setpoint, and use the difference to adjust the input to the process or system in order to achieve the desired output.

However, the feedback loop alone is not sufficient to maintain the desired output. The system needs to include a feedback controller, which is responsible for processing the error signal and generating a control signal to adjust the input to the process or system.

This project includes various implementations of feedback controllers in Java. The controllers range from simple On-Off controllers to more complex Proportional Integral Derivative (PID) controllers, as well as a Gain Scheduling controller. Each controller has been designed to operate within different types of systems and applications.

The project includes the following feedback controllers:

- On-Off Controller
- On-Off Controller with Deadzone
- On-Off Controller with Hysteresis
- Gain Scheduling Controller

You can learn more about in this book by Philipp K Janert: [Feedback Control for Computer Systems: Introducing Control Theory to Enterprise Programmers](amazon.com.br/Feedback-Control-Computer-Systems-Phillipp/dp/1449361692).
