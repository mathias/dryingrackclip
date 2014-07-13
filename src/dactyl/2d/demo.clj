(ns dactyl.2d.demo
  (:require [scad-clj.scad :refer :all]
            [scad-clj.model :refer :all]))

(def primitives
  (union
   (cube 100 100 100)
   (sphere 70)
   (cylinder 10 150)))

(spit "/Users/mathiasx/dev/dactyl/post-demo.scad"
      (write-scad primitives))
