# on-call-manager
On Call Phone Manager is an android application that allows the user to manage incoming phone calls much like Do not Disturb priority mode.
However, the user does not need to know the full phone number of the incomming call.

# How It Works
The user can replace any unkown digits with "#" when they add a contact to the their contact list.

Example: (123) 555-####

The user can then enable the On Call feature of the app. The app compares an incoming phone call's number with numbers in their contact list.
The phone number has to match the digits that the user difines, but "#" can be any digit. 

Incoming Call: (123) 555-4567

Contacts List: (123) 555-####

The above incoming call's number matches the format needed. The app then overrides the audio settings and sets the ringer mode to normal.
