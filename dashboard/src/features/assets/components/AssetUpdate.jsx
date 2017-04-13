import React from 'react'
import { BaseUpdate, FormContainer, FormSection, JsonField } from 'features/shared/components'
import { reduxForm } from 'redux-form'

class Form extends React.Component {
  constructor(props) {
    super(props)
    this.submitWithErrors = this.submitWithErrors.bind(this)
  }

  submitWithErrors(data) {
    return new Promise((resolve, reject) => {
      this.props.submitForm(data)
        .catch((err) => reject({_error: err}))
    })
  }

  componentDidMount() {
    this.props.fetchItem(this.props.params.id).then(resp => {
      if (resp.items.length == 0) {
        this.setState({notFound: true})
      }
    })
  }

  render() {
    const {
      fields: { tags },
      error,
      handleSubmit,
      submitting
    } = this.props

    const item = this.props.item
    const title = <span>
      {'Edit asset tags '}
      <code>{item.alias ? item.alias :item.id}</code>
    </span>

    const tagsString = Object.keys(item.tags).length === 0 ? '{\n\t\n}' : JSON.stringify(item.tags, null, 1)
    const tagLines = tagsString.split(/\r\n|\r|\n/).length
    const JsonFieldHeight = tagLines < 20 ? `${tagLines * 17}px` : '340px'

    return(
      <FormContainer
        error={error}
        label={title}
        onSubmit={handleSubmit(this.submitWithErrors)}
        submitting={submitting} >

        <FormSection title='Asset Tags'>
          <JsonField height={JsonFieldHeight} fieldProps={tags} />
        </FormSection>
      </FormContainer>
    )
  }
}

const validate = values => {
  const errors = {}

  const jsonFields = ['tags']
  jsonFields.forEach(key => {
    const fieldError = JsonField.validator(values[key])
    if (fieldError) { errors[key] = fieldError }
  })

  return errors
}

const fields = [
  'tags'
]

const mapStateToProps = (state, ownProps) => ({
  item: state.asset.items[ownProps.params.id]
})

const initialValues = (state, ownProps) => {
  const item = state.asset.items[ownProps.params.id]
  if (item) {
    const tags = Object.keys(item.tags).length === 0 ? '{\n\t\n}' : JSON.stringify(item.tags, null, 1)
    return {
      initialValues: {
        tags: tags
      }
    }
  }
  return {}
}

export default BaseUpdate.connect(
  mapStateToProps,
  BaseUpdate.mapDispatchToProps('asset'),
  reduxForm({
    form: 'updateAssetForm',
    fields,
    validate
  }, initialValues)(Form)
)
