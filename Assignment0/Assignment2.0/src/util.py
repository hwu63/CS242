import re
import urllib
import urllib.request
import logging
import urllib.error
from bs4 import BeautifulSoup as bs

def grab_age_from_page(this_actor):
    # grab age by regular expression at right side box
    age = this_actor.find('span', {"class": "noprint ForceAgeToShow"})
    if age is None:
        logging.warning("No gross info")
        return 0
    else:
        return int(re.findall(r'\b\d+\b', age.text)[0])



def word_to_int(gross_str):
    #convert english to int

    #get rid of punctuations and get numbers
    data = re.sub(r'[(\xc2|\xa0|+|=|:|$|,)]', '', gross_str)
    digits = float(re.findall(r'([\d\.\d]+)', data)[0])

    if 'billion' in data:
        ret = int(digits*1000000000)
    elif 'million' in data:
        ret = int(digits*1000000)
    elif 'thousand' in data:
        ret = int(digits*1000)
    else:
        ret = digits
    return ret



def grab_grossing_from_page(this_film):
    #grab grossing
    gross = this_film.find_all('th', text="Box office")
    if len(gross) == 0:
        logging.warning("No gross info")
        return 0
    else:
        #found gross info
        gross_str = gross[0].find_next('td').get_text()
        return word_to_int(gross_str)



def is_movie(soup):
    #whether the page is movie by Director info
    cnm = soup.find_all('th', text='Directed by')
    if len(cnm) > 0:
        return True
    else:
        cnm = soup.find_all('th', text='Cinematography')
        if len(cnm) > 0:
            return True
        else:
            return False




def is_actor(soup):
    #whether the page is actor by Occupation info
    role = soup.find_all("td", {'class': 'role'})
    if len(role) > 0:
        for elem in role:
            str_ = elem.get_text()
            if str_ is None:
                return False
            elif str_.find("Actor") >= 0:
                return True
            elif str_.find("actor") >= 0:
                return True
            elif str_.find("Actress") >= 0:
                return True
            elif str_.find("actress") >= 0:
                return True
    else:
        return False




def get_cast(soup):
    actor_list = []
    cast = soup.find_all('span', {'id': ['Cast']})
    if len(cast) == 0:
        return actor_list
    ul = cast[0].find_next('ul')
    urls = ul.find_all('a')
    for link_ in urls:
        actor_list.append(link_.get('href'))
    return actor_list




def get_filmography(name, soup):
    # get films from actor page
    global film_page_link
    logging.info("Movie: " + soup.title.string)

    found_film_page = True
    try:
        film_page_link  = urllib.request.urlopen('https://en.wikipedia.org' + name + '_filmography').read()
    except (ValueError, urllib.error.HTTPError, urllib.error.URLError):
        try:
            film_page_link = urllib.request.urlopen('https://en.wikipedia.org' + name + '_on_screen_and_stage').read()
        except (ValueError, urllib.error.HTTPError, urllib.error.URLError):
            logging.warning("There is no filmography or on screen and stage page")
            found_film_page = False

    if(found_film_page):
        # found page for films.
        logging.info("Found filmography page")
        page = bs(film_page_link, "html.parser")
        span = page.find('span', {'id': ['Film', 'Films']})

        if span is not None:
            #separate film table
            logging.info("Found film table")
            table = span.find_next('table')
        else:
            #merged table
            table = page.find('table')
        return search_table(table)

    else:
        # if no wiki page for filmography, find table or list under filmography
        page = soup
        film_span = page.find('span', {'id': ['Film','Filmography', 'film'] })

        if film_span is not None:
            table = film_span.find_next('table', {'class': re.compile('wikitable')})
            if table is None:
                return search_list(film_span)
            else:
                logging.info("Found film table")
                return search_table(table)
        else:
            return []




def search_list(film_span):
    logging.info("Searching list of film urls...")
    film_list = []
    ul = film_span.find_next('ul')
    if ul is None:
        return film_list
    titles = ul.find_all('a')
    for elem in titles:
        film_list.append(elem.get('href'))
    return film_list




def search_table(table):
    logging.info("Searching for tables...")
    film_list = []
    if table is None:
        logging.info("none table")
        return film_list
    titles = table.find_all('a')
    for elem in titles:
        logging.info("Found" + elem.get('href'))
        film_list.append(elem.get('href'))
        return film_list



def get_movie_year(soup):
    # grab release date by regular expression at right side box
    year = soup.find('span', {'class': 'bday dtstart published updated'})
    if len(year) > 0:
        logging.info("Found release date by tag")
        return year.get_text()[0:4]
    else:
        #search in the content
        title = soup.title.text()
        for s in title.split():
            if s.isdigit() and int(s) > 1800 and int(s) < 2020 :
                logging.info("Found release date by title")
                return int(s)

        #set default
        logging.warning("No release date found. Set to default 1900.")
        return 1900