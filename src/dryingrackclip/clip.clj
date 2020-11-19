(ns dryingrackclip.clip
  (:refer-clojure :exclude [import use])
  (:use [scad-clj.scad])
  (:use [scad-clj.model]))

(def inside-diameter 9.75) ;; mm
(def inside-radius (/ inside-diameter 2))
(def outside-diameter 16) ;; mm
(def outside-radius (/ outside-diameter 2)) ;; mm
(def port-length 20) ;; mm
;;(def block-length 28) ;; mm
(def hook-inside-diameter 18.25) ;; mm
(def hook-inside-radius (/ hook-inside-diameter 2))
(def block-perpendicular-length 45) ;; mm
;;(def block-parallel-length 48) ;; mm
(def block-z 15) ;; mm
(def thickness-cylinders 5) ;; mm

(def block
  (cube
   block-perpendicular-length
   block-perpendicular-length
   block-z))

(def inner-hole
  (cylinder inside-radius (* 10 port-length)))

(def left-port
  (->> inner-hole
       (rotate (deg->rad 90) [0 1 0])
       (translate [(* -1 port-length)
                   (+
                    (* -1 (/ block-perpendicular-length 2)) ;; bottom of block
                    15) ;; amount of offset from bottom
                   0])))

(def top-port
  (->> inner-hole
       (rotate (deg->rad 90) [1 0 0])
       (translate [(-
                    (/ block-perpendicular-length 2)
                    inside-radius
                    6
                    ) ;; in 5mm from edge
                   (/ block-perpendicular-length 2)
                   0])))


(def hook
  (->>
   (union
    (cylinder hook-inside-radius 100)
    (->> (cube hook-inside-diameter hook-inside-diameter (+ block-z 1))
         (translate [0 -10 0])))
   (translate [(- (/ block-perpendicular-length 2)
                  inside-radius
                  8)
               (+ (/ block-perpendicular-length -2)
                  8) ;; y needs to go negative until the hole is 16mm from edge
               0])))

(def negative-space
  (->> (cube 40 40 (+ block-z 1))
       (translate [(/ block-perpendicular-length -2) (/ block-perpendicular-length 2) 0])))


(def cylinder-for-thin-rod
  (difference
   (cylinder outside-radius block-perpendicular-length)
   (cylinder inside-radius (+ block-perpendicular-length 1))))

(def full-clip
  (difference
   (union
    (difference (union
                 cylinder-for-thin-rod
                 (->> cylinder-for-thin-rod
                      (rotate (deg->rad 90) [1 0 0])))
                (->> (cube 40 40 40)
                     (translate [0
                                 (- (/ block-perpendicular-length 2) outside-radius 1)
                                 (- (/ block-perpendicular-length 2) outside-radius 1)])))
    (->>
     (cube outside-diameter (+ (/ block-perpendicular-length 2) 5)
           (+ (/ block-perpendicular-length 2) 5))
     (translate [0
                 (- (/ block-perpendicular-length 4) 4)
                 (- (/ block-perpendicular-length 4) 4)]))
    (->>
     (cylinder outside-radius (+ (/ block-perpendicular-length 2) 4))
     (translate [0 20 6])))
   (->> (union (cylinder (/ 18.25 2) (+ outside-diameter 1))
               (->> (cube 18.25 18.25 18.25)
                    (translate [0 (/ 18.25 2) 0])))
        (rotate (deg->rad 90) [0 1 0])
        (translate [0 20 5]))
   (->> (cube (+ outside-diameter 1) 40 40)
        (translate [0 ;;(/ block-perpendicular-length -2)
                    10 ;; (/ block-perpendicular-length -2)
                    36]))))

(spit "clip-demo.scad"
      (write-scad full-clip))
