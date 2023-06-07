# Ray Tracing App

It's a web application, developed as part of a study project. 
The aim is to optimize Java ray tracing code with threads, and to create an interface that lets you send a scene file and see the return (the image after ray tracing).

The frontend is developed in react, and the backend in nodeJs using express.
Everything is contained in Docker containers. 

## Requirements

To launch the app, you need Docker.
[Install Docker](https://docs.docker.com/desktop/install/mac-install/)

You'll also need NodeJs
[Install NodeJs](https://nodejs.org/en/download)


## Launch the app

    #clone the projet
    git clone https://github.com/BerdinClement/RayTracingApp.git
    
    # access
    cd RayTracingApp
    
    # preparing the front
    cd client && npm i && npm run build
    
    # start the container
    cd .. && docker compose up

After these steps, you're ready to use the application.
Go to the front-end address: http://localhost:8189 
