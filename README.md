# Jeu de manager de club de football

- *Version 1.0*
- *2021*

Contribution : Hamid Kolli <https://github.com/HamidKolli>

Jeu de manager : Le jeu consiste a dériger un club durant une ou plusieurs saisons et de participer à des multipes competitions tout en progressant durant le jeu

## Les classes :

| Description des classes et de leur rôle dans le programme |
|----------------------------------------------------------|
| <b style="color:#d93e1d;">Personne</b> : elle represente toute personne dans notre jeu, elle est abstraite |
| <b style="color:#d93e1d;">Arbitre</b> : hérite de Personne, c'est une personne qui arbitre des matchs |
| <b style="color:#d93e1d;">Joueur</b> : hérite de Personne, elle est abstraite elle permet de representer les trois type de joueurs |
| <b style="color:#d93e1d;">Attaquant</b> : hérite de Joueur, un attaquant est un joueur qui pocéde plus de competence en attaque |
| <b style="color:#d93e1d;">Defenseur</b> : hérite de Joueur, un defenseur est un joueur fort en defense |
| <b style="color:#d93e1d;">Gardien</b> : hérite de Joueur, un gardien est un joueur dont on favorise son niveau gardien |
| <b style="color:#d93e1d;">Formation</b> : cette classe représente la formation d'un club, elle est décrite par la formation et son type |
| <b style="color:#d93e1d;">Club</b> : elle permet de représenter une club, un club contient une liste de joueurs qui interagissent durant les matchs |
| <b style="color:#d93e1d;">Match</b> : Permet de gérer les match de football entre 2 clubs, le match est arbitrer par un arbitre, elle permet de simuler la rencontre|
| <b style="color:#d93e1d;">Transfert</b> : C'est un classe d'association entre entre deux clubs, elle permet d'effectuer le transfert d'un joueur entre les clubs
| <b style="color:#d93e1d;">Tour</b> : Represente un tour de la compétition, elle est décrite par une liste de matchs et le nom du tour |
| <b style="color:#d93e1d;">Competition</b> : C'est une classe générale (abstarite) qui represente une competition de football, elle offre la possibilte de simuler le deroulement d'une competition |
| <b style="color:#d93e1d;">Tournoi</b> : Cette classe décrit un tournoi de football, elle hérite de Competition, sa simulation se base sur un systeme d'elimination direct |
| <b style="color:#d93e1d;">Championnat</b> : Elle represente un championnat de football, elle hérite de Competition, la simulation se base sur systeme de classement des clubs |
| <b style="color:#d93e1d;">Jeu</b> : Permet de gerer la simualtion du jeu, choix de club, simulation de saison (plusieurs competitions), mercato, changement de saison ... |
| <b style="color:#d93e1d;">NonAutoriseException</b> : Une classe qui gerer l'exception d'une opération non autorise dasn le jeu |
| <b style="color:#d93e1d;">FormationNonValideException</b> : Une classe qui gerer l'exception qu'une formation n'est pas valide |
| <b style="color:#d93e1d;">TransfertImpossibleException</b> : Une classe qui gerer l'exception qu'un transfert est impossible à s'effectuer |
| <b style="color:#d93e1d;">Sauvgardable</b> : hérite de Serializable, elle permet de sauvegarder/charger un objet depuis un fichier, elle nous permet de sauvegarder tout le jeu et le recharger |
| <b style="color:#d93e1d;">Humeur</b> : Une enumeration qui represente l'humeur d'une personne (Faible, Normal, Eleve) |
| <b style="color:#d93e1d;">Clavier</b> : Classe fournie par le prof, auquelle on a ajouter quelques methodes, elle gere les entrees du jeu |

## L'uml

| Schéma UML des classes vision fournisseur (lien plantuml)|
|----------------------------------------------------------|
|<a href="http://www.plantuml.com/plantuml/png/rLLDJnin4Btlhx3W4aI2u5gr5K4DfLL3KSEXjshMEoH6k_RYZwWIod-l7SV9DElbGwg4Uajcni_lpRxnd1iBshgcBb2wXbsv1frcZyKnkmHHrkX_N2lTGEr_J6fS8XOZ9gHbKYmHNAmlHgEH3yS_W_tiaJyQOdEqroGy2F5AENz9IFq0MP74e-Tb1LybHhwY5tvL550Qgu5RncimXjsWDafAPA9fQsnGMiDcu9Ou1rr1k34ed5ajv9n9rUoOS0Wmg76jwNYBDDWL3qajXr1xn7pbKecAkRRo4Q9ZSyog-2F4kfA2X2tbWxC3faQfc-_O8hQuYCh6kXHM8yFV5cL5b3uMeyGBx-u41ovi4X4FTJLGlSFkwS2rzJ71oNUQRDC3aYVYIO6GDbnMQhm4Pz547Z17CzoblaYBUedI2YNDDvn3RGXmqijh9N6G-zPVbKEdCmqXBahyf-EVlsQtuzi9-y3EJa-BGtO5PZ65zdripP05DdFK7pVaBfmrprVTA25VKZS5onTcvaohPENyWSIBmeojXGU7-UGdl4DfI39EmcVGbK3fmxk-Zak3agEvgD4Ofp4bNODQHMDiUjVnHPxHy0b2mo9BA6CpGk8eJKq3skDht-n4XQkcHIk29-ZpiVqcq_K9ao-Fe72ld4KTQMOA8vDsdIPtqFeyi75jgqNp2cZAP-s1O06KbUizI_sIYZkDo43b6LUY4TAzkbwlhOFnFKvJjo9lmOqJne3adPBegpIqFbSgLID8XWyEQZFuNjuZjqoLzpI9nf8ROwynHiXJuh6r-NjtN7VDIY3o-NwVvx2fsVFODDy_9tlOT5OewbucDrhK3UjfekTJYCvQTnUHM7ZOBMJ7B-msNYE4DNMuUr6ZfwjgqFlcBO0lXgp7uISd9vl-60gR9BqUjKjEOwEM-2_uhTBsZGu1QTEwIKuFosQRVCltj3kST4_l3-PMMBmsfBh2Tid_KDW6loFhHQj_8Zduk_35gpsWeudBDLtzbasZwu8AUdA_OIpgsyVPXHQ4Fx-xQPlo5FBVELqPtw7d5qgV9yx3uclgFm00">Diagramme des personnes</a> |
|<a href="http://www.plantuml.com/plantuml/png/nLHXYnit4FtkNo7IBskw2xbBeH3Q42TdXmi-EvnzK2WboDgnJyUkj1cDWiEH_rvfvTNAUrjwXKA-t7dVctaQZMRUMqCSoTRLXEyC8HV4HCMDOH-r1OkJIVXQQAmvIQsOh9iAQb1as8RRht3WMF9T1UnnyZDxDEIYn7Sc5R7L-lPcjcI_ivT3PhrOp5URwuNZBeRSrJnoboct8PJguEdPtNBxUU75jzVhbOjxySkhLoycHPjEtngO55tqlYixi7_-nIff2AvW3yg0HNEEpuZu5yiL3V0RMKcmnm0oVsC9NdA6oByjNSHlruhW0FWcn6ybsOfBT9KKXRj4x5ush2kFHUV_hZPrKb-rB9cuvyhnpv5zViu_HpxHGd13m0LWIpsqaxBHI5nHpdfehWWTD9qK8Qq1h2r1adIHXb_wm9tM5N354DeJddL0dhwL4qclyJzr_00A4F2JTgp9_1d3mJm0tKAJ9PNahDJMRqRDZ-5KC0qNNhHB8Xr-UCKEu_kz5FU0MrUYmvFfOQBIAc3zeP7rU9XFe9WRod78ydpk1sGov2DClnRf6H_26dXeh94k8bY8YTtqwTyxHtbVsTqppIIqCQx-kxkh3_EjzuwNJvpbPlR7vu_hk_dTxSORoEKJ_dgLy1U_zgrKkfuKtL4xMxhwat4KzreQQKwtXwCyW19m2r-iDDsM-njblLf-BfQFPSRvucwW2J1qB0l_JaC-dVmKop2sQL3Muu7d3n6eDmn12Di00deue2OTqvLrUjgScF-WCRuzGy4fSv9T_RoT9CZQnnVkJzwrN-Yw0PB1Y8gH6ae_koL2bleKfZuL9mYI81D1J3ObAGaCRSPZYXzkjkvtvpoXgIshsfzHmJOb9vWJZmypqj9nvpQffK8mqxwo2ZXcesQLjiyPGsMozUu1136zUqZ91cnR52xXXfEu7wqi6ifArpk4qoNcHr7PyQk4JKlCCAJqZfDqQV3IxyCQ9-C1BNRC1elk0yBwUjbtxGRtMTmUl6a1deWGwU4OKifQAZkCSEYvyYShZU5A39n-QEmZIndTENm7RsqReuk21V0rgvA8detIh_k976T7Tgipy2roPVQ0n1vR0onw99iclXlwGISE1gQLi-W56nJ9rnKOYXRQe3o6D7_Bjw1AMrT_0m00">Diagramme des clubs</a>|
|<a href="http://www.plantuml.com/plantuml/png/xLNDRXit4BxlKn39HO6rHNDLMo6kegGDB5U8x5uBQdSiqU2IM_uODWozUpbBqQHOQjlwqaiEjgZvds--6NtGXYfZErurd6fDLjGqUyAwdaC7mcYoeVO1TbIrTCk1F5SrsHZ5n8uew1LePqCDa-9dQ_HP7GUjhOAp1XkhUn2jHGCc316osoh0a0jkjnzbnnh6eQXSFXfG3AogwhgkBdpz4zIG9l6TaY2A7bDogRRCA23KVxvp_W-IjUHUMb2JhPGSg20TXhosV1gARsJNWs5OkNC9qXsOJICLJ1APaVw1Cb-1i_gTCl6d1QjEf6cdkIBMZ9fGDYgFpybf7LzFq4Kb8eUQwoAoPosonCyQ8pQ7m1Bn04exfFpN0ISq2y21Dckgd0FhCSMXydPlthyXigv36xC0VwR6zBDqYESD8WcEBBwHLesHE825P3eI1rqcKOhaKFJfYcdpuv1sJhXxm_3MhsHA7XBnlefHJp6EnbZQFAATAupxhwT9HqK2PWJHbhiyaz5nONX4sezxbW9GLNIhZQAD8HxnHUnq5FZONoijoZukFrtUNjtyyNMv-6srNbvlbkGdylxxqn5T16i50wH2Gs0iPMBvsEFuHGFLMtyYR0TAOlbE4DhhGs-9h8LxUdbtnvezWET1saXAXxyNC2taJrbpfFSyq5kdIkJkaL7u70ZrHU9HmF6iW77ktauILk-pmz2bIBL7qQJiwpPuy0rCfRvDDSplrZ2NcxvOF8Tf7X9Q8twPDkNA9oPOhaONB08_5-wF2fEPX3CJCVjCTm8WdkpCFcBcUnoMnZzrEglChNniypvjtr83WVMq42YVMLXPFxQH_I5EayAnLj102yAS7DiNegAqH9vuu_N83fS8xNSutSjmE37VZ9YqgDIkU9nrUbg5P0GU3OZsPF5nluF-zRjJMe_HlNfNrVdLJ4vi8uKsoZ85wQLzTQgBqeAKjkBSAlmh_lyNqfUPFahlSKwNVpuELPXyHgFONx28lyu9he4_YptjUbU8eEOCXzwINwZUhsW_L3Sblme3Et0O83fwBHs2-eTmhMEq_ubnES6-80O0toZsJnHBDxN_RYcCUie6FYT6CL3HfSmBZBs8rCY2Et2yIJgTx9Gka9dGWJ2niOO3LMTseGuGhwNsA2NG7ggKud5HU9A_-k1uRpl-5m00">Diagramme des competitions et des matchs</a>|
|<a href="http://www.plantuml.com/plantuml/png/XLDDJyCu4BttLrWz5Ir5siiUq7uGe4YWKh7RmhMQ9DDsa6D7CtQtRCL_NxkrqgG4SKhyvisRySppZJXWvoizA3I8g3iDMq9M-y5OxKFCKV6fRlrgXOpbFmXbY95HVoY3_vxXemWzcjgxkME4Ag7p3t5ONP6vk1YCn-EWcsgCBWQ_d6gGSKg0X7XYN8m64J9hLQ58XrXQdsjCtFl3uLlk4K_SAQrDBxMflqPtjx6qQvDWZPU7O0MxJiBMKgaARGN3HJu7Wy11Na61QWv-YslW4cAZk7DeIb5pP09D_nt0_HSbWMtJt8eqpg32opCfcx-ZQpTn5YysmEilAfs4t5gDO49N9Aw76Yu2kJY6mgda25lLwCYHDSaZJ-WLLRN62esJinxsFVD_U9nEbhFiURAypnRPyZQRJuA3Xk_ehyfy-5t2F7jhQ88yFD_Mtw_PRF7wl7oQl0RQRst3nWu37A0aM0lwqhve-40EQ4p7NOrCQ0fC03baYDUS0uaroQn1gclK04IZ7F5kpRG0xWam9iEd1GIgZQQyqpwFzFQOrRlro2r0HXapV4n9tEz_DOPg4VvKHOiTv98xDs12Em-MAuYHjEus-GKuD4arDaaLSW7ETfhLO8hKVqiuhlyugmuPvV2AXEG0fuSrQSPzaX5qBwt5Z7gMrN1zNOB3baftXz6vST33NuVAZiSgpbLTgo9yKZCnV0njeAh96WCk44w74sDXFHjBryeTVteYXVKr9ZoLEz1bYX8Ffr36EJd69fhE3Q6zn-lEQXWBBD4u7AI7DxCcyyuoIHX4WNLaDNPeaFW4-wZD-lcmip2n9Dlu8oIzWQRo9_MUZ1-BB1YCh93TOrLR4ShrZrM-J-ahSnCKVAN_1m00">Diagramme jeu et outils</a>|

- **NB :<a href="http://www.plantuml.com/plantuml/png/VPDBRzim3CVl-XH2xptiwbEBj7iCwr2WnUwCmpG4zF18QWZswHVBDkkviGzn9Ft-ueF--quKM4iC3lQY3AtwDe28VqQMd19wYbt0Y4d5lq3vWs_01zW7TAQu-NJZy-sj_reY5cxygVul0txF1GjxF2kcmoo-6_0zxqaPF_09gyBl0abDCCGRyGCUCOd5N-8lm0V2j8GEKubZsSvHKkGZjFX_dtRf1PaWqD_Q_1ZuS-O8IZcjZyaaTKP3viQ_3HMjq42YQ2tBbiewtrGzKI0ivot9Asa9G6ozxKBPhxTLwOSWmqRUgILi_75QRbDBWrR9AqEI8x9E1q-WxMcrDkygDr-Ercey38i5xgjfV7jvFkH8BOLrmUDPaGaBMsMvyFgCUbeZr7ZZYEfQtFBpsENOeTAsbsOYgwvFqdYzF9yPMpnWqhdkwWHaNd_AP1wVNtGJ_9XfT_b1KbFOb8OM-wKuyz49OaSv9T1hUToSMngp4oVpmMYrdpdT5yrCWe_d5hi-Y0Msa-K7Uhdr2m8TjkxufT--nIwBq3vitRBgiAo_kHxE_UG7G-rEcOIannNDPd4Hl6zCCRsnnkkmk4BQN3givvQkokK7Ih-PaFd2wBWNTuVfK6Bu1m00"> Voici le diagramme de l'hierarchie complete</a>**

## Présentation du projet 

Ce projet consiste en un jeu de gestion d'un club de football, l'utilisateur poura choisir son club selon son experience, choisir ses joueurs et mettre en place des strategies et des formations pour gagner des matchs, des championnats et tournois nationaux et internationaux, tout en progressant au fil du jeu. Les compétences des joueurs sont affectées à chaque participation ce qui affecte les resultats des rencontres, a chaque fin de competition un club vainqueur sera elu, il obtiendra une recompense, ainsi qu'un meilleur buteur, joueur et gardien seront choisis selon leurs parcours respectifs (nombre de buts, nombre de points ...), a chaque fin de saison l'utilisateur sera recomposé selon son classement dans le championnat ou il participe, et les tournois gagnés. L'utilisateur poura acheter et vendre des joueur pendant la periode du mercato.

Le jeu se base sur un systeme de sauvegarde et de restoration, a chaque evenement le jeu sera sauvgarder automatiquement.

Le jeu comporte deux championnats de huits clubs et quatre arbitres, deux coupe nationales et deux tournois internationaux (ligue des champion et europa ligue), chaque championnat a une coupe organisée entre ses clubs, tandis que les tournois internationaux sont organisés aléatoirement pour la première saison et pour les autres saisons les clubs qui y participent seront selectionnés en fonction de leurs classement dans leur championnat respectif, les quatre premièrs club de chaque championnats joueront a la ligue des champion et les quatre derniers a l'europa ligue. l'utilisateur peut voir que les competitions ou il participe.  

## TODO
- Ameliorer l'AI du jeu
- Ajouter plus de clubs/championnats
- Integerer le multi-joueurs
