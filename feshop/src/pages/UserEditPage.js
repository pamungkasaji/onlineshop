import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Form, Button } from 'react-bootstrap'
import { useDispatch, useSelector } from 'react-redux'
import Message from '../components/Message'
import Loader from '../components/Loader'
import FormContainer from '../components/FormContainer'
import { getUserDetails, updateUser } from '../actions/userActions'

const UserEditPage = ({ match, history }) => {
  const id = match.params.id

  const [name, setName] = useState('')
  const [username, setUsername] = useState('')
  const [admin, setAdmin] = useState(false)

  const dispatch = useDispatch()

  const userDetails = useSelector((state) => state.userDetails)
  const { loading, error, user } = userDetails

  const userUpdate = useSelector((state) => state.userUpdate)
  const { loading: loadingUpdate, error: errorUpdate, success: successUpdate } = userUpdate

  useEffect(() => {
    if (successUpdate) {
      dispatch({ type: 'USER_UPDATE_RESET' })
      history.push('/admin/userlist')
    } else {
      if (!user.name || user.userId !== id) {
        dispatch(getUserDetails(id))
      } else {
        setName(user.name)
        setUsername(user.username)
        setAdmin(user.admin)
      }
    }
  }, [dispatch, history, id, user, successUpdate])

  const submitHandler = (e) => {
    e.preventDefault()
    dispatch(updateUser({ userId: id, name, username, admin }))
  }

  return (
    <>
      <Link to='/admin/userlist' className='btn btn-light my-3'>
        Go Back
      </Link>
      <FormContainer>
        <h1>Edit User</h1>
        {loadingUpdate && <Loader />}
        {errorUpdate && <Message variant='danger'>{errorUpdate}</Message>}
        {loading ? (
          <Loader />
        ) : error ? (
          <Message variant='danger'>{error}</Message>
        ) : (
              <Form onSubmit={submitHandler}>
                <Form.Group controlId='name'>
                  <Form.Label>Name</Form.Label>
                  <Form.Control
                    type='name'
                    placeholder='Enter name'
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                  ></Form.Control>
                </Form.Group>

                <Form.Group controlId='username'>
                  <Form.Label>Username</Form.Label>
                  <Form.Control
                    type='username'
                    placeholder='Enter username'
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                  ></Form.Control>
                </Form.Group>

                <Form.Group controlId='admin'>
                  <Form.Check
                    type='checkbox'
                    label='Is Admin'
                    checked={admin}
                    onChange={(e) => setAdmin(e.target.checked)}
                  ></Form.Check>
                </Form.Group>

                <Button type='submit' variant='primary'>
                  Update
            </Button>
              </Form>
            )}
      </FormContainer>
    </>
  )
}

export default UserEditPage