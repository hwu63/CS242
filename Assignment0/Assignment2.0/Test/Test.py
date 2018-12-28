import unittest
import src.actor as actor
import src.movie as movie
import src.graph as G
import src.query as Q
import src.jsonfy as js

class TestActor(unittest.TestCase):
    def test_actor(self):
        s1 = actor.actor('/wiki/aho')
        s1.set_age(20)
        assert isinstance(s1, actor.actor)
        assert s1.valid is False
        assert s1.name == 'aho'
        assert s1.age == 20

class TestMovie(unittest.TestCase):
    def test_movie(self):
        m1 = movie.movie('/wiki/kalklaklaklaklakla')
        m1.set_year(2000)
        assert isinstance(m1, movie.movie)
        assert m1.valid is False
        assert m1.title == 'kalklaklaklaklakla'
        assert m1.year == 2000


class TestGraph(unittest.TestCase):
    def test_graph(self):
        g = G.graph()
        assert isinstance(g.actors, dict)
        assert isinstance(g.movies, dict)
        g.add_actor('/wiki/aho')

        s1 = actor.actor('/wiki/aho')
        s1.set_age(20)

        m1 = movie.movie('/wiki/kalklaklaklaklakla')
        m1.set_year(2000)
        g.add_movie(m1)

        g.add_edge(s1, m1)
        g.add_edge(m1, s1)
        g.adjacency_list()

        l1 = [s1, m1]
        g.add_vertices(l1)






class TestJsonfy(unittest.TestCase):
    def test_restore(self):
        actor_dict, movie_dict = js.restore('data_test.json')
        assert len(actor_dict) > 0
        assert len(movie_dict) > 0
        print(actor_dict)
        print(movie_dict)

    def test_make_file(self):
        # warning
        s1 = actor.actor("/wiki/aha")
        s1.set_age(-1)

        # good
        s2 = actor.actor('/wiki/Willie_Aames')
        s2.set_age(90)
        s2.set_gross('128238')
        s3 = actor.actor('/wiki/Quinton_Aaron')
        s3.set_age(100)

        s4 = actor.actor("/wiki/aho")
        s4.set_age(20)

        # warning
        m1 = movie.movie('/wiki/blablablbalblbla')
        m1.set_year(1800)

        # good
        m2 = movie.movie('/wiki/kalklaklaklaklakla')
        m2.set_year(2000)
        m2.set_gross(1000000)
        m3 = movie.movie('/wiki/heihiehiehiehihei')
        m3.set_year(1950)
        m3.set_gross(4000)

        # mutual links
        s2.add_movie(m2)

        s3.add_movie(m2)
        s3.add_movie(m3)

        m2.add_actor(s2)
        m2.add_actor(s3)

        m3.add_actor(s3)

        actors_ = []
        movies_ = []

        actors_.append(s1)
        actors_.append(s2)
        movies_.append(m1)
        movies_.append(m2)
        actors_.append(s3)
        movies_.append(m3)

        js.jsonfing(actors_, movies_, 'data_test.json')

class TestQuery(unittest.TestCase):
    def test_query(self):
        qr = Q.query('data_test.json')

        actor_list = qr.find_oldest_x_actors(2)
        #assert actor_list[0].name == 'Quinton_Aaron'
        print(actor_list[0].name)

        print(qr.list_top_x_actors_by_gross(1)[0].name)

        print(qr.find_movie_gross('heihiehiehiehihei'))
        print(qr.find_movie_gross('not exist'))

        print(qr.movie_list_of_actor('Quinton_Aaron'))
        print(qr.actor_list_of_movie('heihiehiehiehihei'))






