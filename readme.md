关于思考题：
1：在数据库主键设置为自增的情况下，插入数据是自定义主键，是否有效果？会发生什么？
经过测试，在设置数据库的主键为自增时，在插入数据的时候的即使为该字段自定义了值，数据入库后也没有效果。数据是可以落库的，但是主键值还是按照自增的策略增加，
并没有使用自定义的主键值，暂时也没有找到相关的方法.
