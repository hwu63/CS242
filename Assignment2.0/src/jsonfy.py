import json
from src import actor as A
from src import movie as M

def jsonfing(actor_list, movie_list, filename_):
    file = []
    aj_list = {}
    mj_list = {}
    file.append(mj_list)
    file.append(aj_list)


    for item in movie_list:
        if item is None:
            continue
        this_movie = {}
        this_movie['class'] = 'Movie'
        this_movie['year'] = item.year
        this_movie['gross'] = item.gross
        this_movie['title'] = item.title
        this_movie['actors'] = []
        for actor in item.actors:
            this_movie['actors'].append(actor.name)
        mj_list[item.title] = this_movie

    for item in actor_list:
        if item is None:
            continue
        this_actor = {}
        this_actor['class'] = 'Actor'
        this_actor['age'] = item.age
        this_actor['name'] = item.name
        this_actor['total_gross'] = item.total_gross
        this_actor['movies'] = []
        for film in item.movies:
            this_actor['movies'].append(film.title)
        aj_list[item.name] = this_actor

    file_ = open(filename_, 'w')
    file_.write(json.dumps(file, indent=4, separators=(',', ': ')))

def restore(filename_):

    actor_dict = dict()
    movie_dict = dict()
    json_parsed = json.load(open(filename_, 'r'))

    #for every actor create new
    for actor_ in json_parsed[1].values():
        # Create new actor object from the parsed data
        new_actor = A.actor(actor_['name'])
        new_actor.set_age(actor_['age'])
        new_actor.set_gross(actor_['total_gross'])

        actor_dict[actor_['name']] = new_actor


    #for every movie create new
    for movie_ in json_parsed[0].values():
        new_movie = M.movie(movie_['title'])
        new_movie.set_year(movie_['year'])
        new_movie.gross = movie_['gross']
        cast_list = movie_['actors']
        for actor in cast_list:
            if actor not in actor_dict:
                continue
            new_movie.add_actor(actor_dict[actor])
            movie_dict[movie_['title']] = new_movie

    #mutual link again
    for actor in json_parsed[1].values():
        film_list = actor['movies']
        for film in film_list:
            if film not in movie_dict:
                continue
            actor_dict[actor['name']].add_movie(movie_dict[film])

    return actor_dict, movie_dict
