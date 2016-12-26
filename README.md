# chariot

Personal project for managing home theater and home automation devices attached to the home network. 
The intention is to include a universal REST-based remote control system for in-home devices. Currently, has a simple command-line interface of the form:

    chariot> devices
    denon, kuro, xbox, appletv, plex
    chariot> denon
    commands: power-on, power-off, volume-up, volume-down
    chariot> denon power-on
    denon: PWRON

This is a very rough initial cut. Currently, just a few things I'm tinkering with. Once I have 
it reliabily communicating with several devices at home, I can refactor to be more extensitble
(based on the learning).

I've got two devices working at home (IP control). Next step is getting a video source integrated (plex/appletv) and a simple macro system to put all connected equipment in the defined state:

    plex:
        denon on
        kuro on
        appletv on
        denon input appletv
        appletv open plex
        plex show tv-latest
        
And to support simple dependency rules:

    plex: appletv
    appletv: denon
    xbox: denon
    denon: kuro
    
    play: on
    
So that 'living-room plex play Miles Davis' (as an Echo command, "living-room" mapping to extenal control lambda) will result in the following commands generated:

    load living-room
    kuro on
    denon on
    appletv on
    plex on (i.e. appletv open plex)
    plex play Miles Davis
    
## End-to-End Prototype

1. Iniate macro with Alexa: "Alexa, tell the house to queue The Force Awakens."
2. Tell plex to display "The Force Awakens" (display info window)
3. Set default viewing for plex
4. Set lighting in whole house scheme for plex/movies.
5. Alexa reples with ready
6. "Alexa, tell house to start movie."