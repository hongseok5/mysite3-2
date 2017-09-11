package com.bigdata2017.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bigdata2017.mysite.exception.UserDaoException;
import com.bigdata2017.mysite.vo.UserVo;

import oracle.jdbc.pool.OracleDataSource;

@Repository
public class UserDao {
	
	@Autowired
	private OracleDataSource oracleDataSource;
	
	@Autowired
	private SqlSession sqlSession;
	
	public int update( UserVo userVo ) {
		
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = oracleDataSource.getConnection();
			
			if( "".equals( userVo.getPassword() ) ) {
				String sql = 
					" update member" + 
					"    set name=?," + 
					"        gender=?" + 
					"  where no=?";
				pstmt = conn.prepareStatement(sql);
	
				pstmt.setString(1, userVo.getName() );
				pstmt.setString(2, userVo.getGender() );
				pstmt.setLong(3, userVo.getNo() );
			} else {
				String sql = 
						" update member" + 
						"    set name=?," + 
						"        gender=?," + 
						"        password=?" + 
						"  where no=?";
				pstmt = conn.prepareStatement(sql);
		
				pstmt.setString(1, userVo.getName() );
				pstmt.setString(2, userVo.getGender() );
				pstmt.setString(3, userVo.getPassword() );
				pstmt.setLong(4, userVo.getNo() );				
			}

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error :" + e);
		} finally {
			// 자원 정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;	
	}
	
	public UserVo get( String email ) {
		return sqlSession.selectOne( "user.getByEmail", email );
	}
	
	public UserVo get( Long userNo ) {
		return sqlSession.selectOne( "user.getByNo", userNo );
	}
	
	public UserVo get( String email, String password ) throws UserDaoException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		map.put("password", password);
		UserVo vo = sqlSession.selectOne("user.getByEmailAndPassword", map);
		
		return vo;
	}
	
	public int insert( UserVo vo ) {
		int count = sqlSession.insert("user.insert", vo);
		return count;
	}	
}
