import urllib
import urllib.request
import re
from bs4 import BeautifulSoup as bs

#create age list
age_dict = {}
actor_counter = 0

#actor list webpage
wiki_actor_list = "https://en.wikipedia.org/w/index.php?title=Category:21st-century_American_male_actors&pageuntil=Banner%2C+David%0ADavid+Banner#mw-pages"
soup = bs(urllib.request.urlopen(wiki_actor_list), "html.parser")

#get to first actor page
wiki = "https://en.wikipedia.org"
actor = soup.find('a', {"title":"Willie Aames"}).get('href')
link = wiki+actor
name ="Willie Aames"
actor_counter+=1

#grab age
this_actor = bs(urllib.request.urlopen(link), "html.parser")
age = this_actor.find('span', {"class": "noprint ForceAgeToShow"}).text
age = [int(s) for s in re.findall(r'\b\d+\b', age)]
age_dict.update({name: age[0]})

#traverse all actors
while(name != "Ogie Banks"):
    actor = soup.find('a', {"title":name}).findNext('a')
    link = wiki + actor.get('href')
    print(link)
    name = actor.get('title')

    this_actor = bs(urllib.request.urlopen(link), "html.parser")
    age = this_actor.find('span', {"class": "noprint ForceAgeToShow"})
    if (age != None):
        age = age.text
        age = [int(s) for s in re.findall(r'\b\d+\b', age)]
        age_dict.update({name: age[0]})
        actor_counter+=1
print(age_dict)